package com.example.hype;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by Gavin on 15-Oct-15.
 */
public class _FileCache
{
    private File cacheDir;

    public _FileCache(Context context)
    {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            cacheDir = new File(Environment.getExternalStorageDirectory(), "HypeLazyList");
        }
        else
        {
            cacheDir = context.getCacheDir();
        }

        if(!cacheDir.exists())
        {
            cacheDir.mkdir();
        }
    }

    public File getFile(String url)
    {
        String filename = String.valueOf(url.hashCode());
        File f = new File(cacheDir, filename);
        return f;
    }

    public void clear()
    {
        File[] files = cacheDir.listFiles();
        if(files == null)
        {
            return;
        }

        for(File f : files)
        {
            f.delete();
        }
    }

}
