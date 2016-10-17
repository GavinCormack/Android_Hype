package com.example.hype;

import android.graphics.Bitmap;
import android.os.DropBoxManager;
import android.util.Log;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Gavin on 15-Oct-15.
 */
public class _MemoryCache
{
    private static final String TAG = "MemoryCache";

    private Map<String, Bitmap> cache = Collections.synchronizedMap(new LinkedHashMap<String, Bitmap>(10, 1.5f, true));

    private long size = 0;

    private long limit = 1000000;

    public _MemoryCache()
    {
        setLimit(Runtime.getRuntime().maxMemory() / 4);
    }

    public void setLimit(long new_limit)
    {
        limit = new_limit;
        Log.i(TAG, "Memory = " + limit);
        Log.i(TAG, "Memory = " + limit / 1024. / 1024. + "MB");
    }

    public Bitmap get(String id)
    {
        try
        {
            if(!cache.containsKey(id))
            {
                return null;
            }
            return cache.get(id);
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public void put(String id, Bitmap bm)
    {
        try
        {
            if(cache.containsKey(id))
            {
                size -= getSizeInBytes(cache.get(id));
            }
            cache.put(id, bm);
            size += getSizeInBytes(bm);
            checkSize();
        }
        catch(Throwable e)
        {
            e.printStackTrace();
        }
    }

    private void checkSize()
    {
        Log.i(TAG, "Cache size = " + size + " length = " + cache.size());

        if(size > limit)
        {
            Iterator<Map.Entry<String, Bitmap>> iter = cache.entrySet().iterator();

            while(iter.hasNext())
            {
                Map.Entry<String, Bitmap> entry = iter.next();
                size -= getSizeInBytes(entry.getValue());
                iter.remove();
                if(size <= limit)
                {
                    break;
                }
            }
            Log.i(TAG, "Clean Cache. New Size = " + cache.size());
        }

    }

    public void clear()
    {
        try
        {
            cache.clear();
            size = 0;
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    private long getSizeInBytes(Bitmap bm)
    {
        if(bm == null)
        {
            return 0;
        }
        return bm.getRowBytes() * bm.getHeight();
    }


}
