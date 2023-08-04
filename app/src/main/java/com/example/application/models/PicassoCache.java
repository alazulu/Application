package com.example.application.models;

import android.content.Context;

import com.squareup.picasso.LruCache;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class PicassoCache {
    private static Picasso picassoInstance = null;

    private PicassoCache(Context context) {
        int cacheSize = 100 * 300* 300; // 50 MB
        LruCache cache = new LruCache(cacheSize);
        OkHttp3Downloader downloader = new OkHttp3Downloader(context.getApplicationContext());
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(downloader);
        picassoInstance = builder.build();
    }

    public static Picasso getPicassoInstance(Context context) {
        if (picassoInstance == null) {
            new PicassoCache(context);
        }
        return picassoInstance;
    }
}
