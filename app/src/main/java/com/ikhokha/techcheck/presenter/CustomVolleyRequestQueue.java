package com.ikhokha.techcheck.presenter;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;

/**
 * This custom class is for use with the ImageLoader
 */
public class CustomVolleyRequestQueue {
    private static CustomVolleyRequestQueue customVolleyRequestQueue;
    private static Context context;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    private CustomVolleyRequestQueue(Context context) {
        CustomVolleyRequestQueue.context = context;
        requestQueue = getRequestQuee();
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<>(20);
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    public RequestQueue getRequestQuee(){
        if(requestQueue == null) {
            Cache cache = new DiskBasedCache(context.getCacheDir(), 10*1024*1024);
            Network network = new BasicNetwork(new HurlStack());
            requestQueue = new RequestQueue(cache, network);
            //Start volley request queue
            requestQueue.start();
        }
        return requestQueue;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public static synchronized CustomVolleyRequestQueue getInstance(Context context) {
        if (customVolleyRequestQueue == null) {
            customVolleyRequestQueue = new CustomVolleyRequestQueue(context);
        }
        return customVolleyRequestQueue;
    }

}