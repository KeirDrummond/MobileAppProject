package com.example.ratemypet;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ItemFinder {
    // Utility class
    // Used to get items using JSON.

    // https://stackoverflow.com/questions/28172496/android-volley-how-to-isolate-requests-in-another-class

    private static ItemFinder instance = null;

    public RequestQueue requestQueue;

    private ItemFinder(Context context)
    {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized ItemFinder getInstance(Context context)
    {
        if (null == instance)
            instance = new ItemFinder(context);
        return instance;
    }

    public static synchronized ItemFinder getInstance()
    {
        return instance;
    }

    public void GetRandomItem(final MyListener<GalleryItem> listener) {
        String url = "https://dog.ceo/api/breeds/image/random";

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String url = response.get("message").toString();
                            GalleryItem item = new GalleryItem("title", url);
                            listener.getResult(item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Everybody panic
                Log.d("myTag", error.toString());
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        requestQueue.add(jsonObjectRequest);

        GalleryItem galleryItem = new GalleryItem("title", "url");
    }

}
