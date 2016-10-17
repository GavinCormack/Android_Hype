package com.example.hype;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Gavin on 15-Oct-15.
 */
public class _ImageLoader
{
    _MemoryCache memoryCache = new _MemoryCache();

    _FileCache fileCache;

    private Map<ImageView, String> imageviews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());

    ExecutorService executorService;

    Handler handler = new Handler();

    final int stub_int = R.drawable.mona_lisa;


    public _ImageLoader(Context context)
    {
        fileCache = new _FileCache(context);

        executorService = Executors.newFixedThreadPool(5);
    }


    public void displayImage(String url, ImageView iv)
    {
        imageviews.put(iv, url);

        Bitmap bm = memoryCache.get(url);

        if(bm != null)
        {
            iv.setImageBitmap(bm);
        }
        else
        {
            queuePhoto(url, iv);
            iv.setImageResource(stub_int);
        }

    }

    private void queuePhoto(String url, ImageView iv)
    {
        _PhotoToLoad p = new _PhotoToLoad(url, iv);

        executorService.submit(new _PhotoLoader(p));
    }

    private class _PhotoToLoad
    {
        public String url;
        public ImageView iv;

        public _PhotoToLoad(String url, ImageView iv)
        {
            this.url = url;
            this.iv = iv;
        }
    }

    class _PhotoLoader implements Runnable
    {
        _PhotoToLoad ptl;

        public _PhotoLoader(_PhotoToLoad p)
        {
            ptl = p;
        }

        @Override
        public void run()
        {
            try
            {
                if(imageViewRefused(ptl))
                {
                    return;
                }

                Bitmap bm = getBitmap(ptl.url);

                memoryCache.put(ptl.url, bm);

                if(imageViewRefused(ptl))
                {
                    return;
                }

                _BitmapDisplayer bd = new _BitmapDisplayer(bm, ptl);

                handler.post(bd);
            }
            catch(Throwable e)
            {
                e.printStackTrace();
            }
        }
    }

    private Bitmap getBitmap(String url)
    {
        File f = fileCache.getFile(url);

        Bitmap bm = decodeFile(f);

        if(bm != null)
        {
            return bm;
        }

        try
        {
            Bitmap b = null;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);

            InputStream is = conn.getInputStream();
            OutputStream os = new FileOutputStream(f);

            _Utils.copyStream(is, os);

            os.close();
            conn.disconnect();

            b = decodeFile(f);

            return b;
        }
        catch(Throwable e)
        {
            e.printStackTrace();

            if(e instanceof OutOfMemoryError)
            {
                memoryCache.clear();
            }
            return null;
        }
    }


    private Bitmap decodeFile(File f)
    {
        try
        {
            BitmapFactory.Options opts1 = new BitmapFactory.Options();
            opts1.inJustDecodeBounds = true;

            FileInputStream stream1 = new FileInputStream(f);
            BitmapFactory.decodeStream(stream1, null, opts1);
            stream1.close();

            final int REQUIRED_SIZE = 85;

            int width_tmp = opts1.outWidth, height_tmp = opts1.outHeight;
            int scale = 1;

            while(true)
            {
                if(width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                {
                    break;
                }

                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            BitmapFactory.Options opts2 = new BitmapFactory.Options();
            opts2.inSampleSize = scale;
            FileInputStream stream2 = new FileInputStream(f);
            Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, opts2);
            stream2.close();
            return bitmap;

        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }


    private boolean imageViewRefused(_PhotoToLoad ptl)
    {
        String tag = imageviews.get(ptl.iv);

        if(tag == null || !tag.equals(ptl.url))
        {
            return true;
        }
        return false;
    }

    class _BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        _PhotoToLoad ptl;

        public _BitmapDisplayer(Bitmap b, _PhotoToLoad p)
        {
            bitmap = b;
            ptl = p;
        }


        @Override
        public void run()
        {
            if(imageViewRefused(ptl))
            {
                return;
            }

            if(bitmap != null)
            {
                ptl.iv.setImageBitmap(bitmap);
            }
            else
            {
                ptl.iv.setImageResource(stub_int);
            }
        }
    }

    public void clearCache()
    {
        memoryCache.clear();
        fileCache.clear();
    }


}
