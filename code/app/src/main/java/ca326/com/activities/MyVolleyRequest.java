package ca326.com.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;


public class MyVolleyRequest {

    private static MyVolleyRequest myVolleyRequest;
    private static Context context;
    private RequestQueue queueRequest;
    private ImageLoader loadImage;


    // This will retrieve each image in the database and get the url
    private MyVolleyRequest(Context context) {
        this.context = context;
        this.queueRequest = getRequestQueue();

        loadImage = new ImageLoader(queueRequest,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

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

    public static synchronized MyVolleyRequest getInstance(Context context) {
        if (myVolleyRequest == null) {
            myVolleyRequest = new MyVolleyRequest(context);
        }
        return myVolleyRequest;
    }

    public RequestQueue getRequestQueue() {
        if (queueRequest == null) {
            Cache cache = new DiskBasedCache(context.getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            queueRequest = new RequestQueue(cache, network);
            queueRequest.start();
        }
        return queueRequest;
    }

    public ImageLoader getImageLoader() {
        return loadImage;
    }
}
