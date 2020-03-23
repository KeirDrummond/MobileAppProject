package com.example.ratemypet;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class GalleryFragment extends Fragment {

    JSONObject jsonObject;
    JsonObjectRequest jsonObjectRequest;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        GetJSON();
        //FakeTable();
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    private void GetJSON() {
        RequestQueue queue = Volley.newRequestQueue(this.getContext());
        String url = "https://dog.ceo/api/breeds/image/random";
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        jsonObject = response;
                        FakeTable();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("myTag", error.toString());
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(jsonObjectRequest);
    }

    private void FakeTable() {
        try {
            String imageurl = jsonObject.getString("message");
            Log.d("myTag", imageurl);
            ImageView img;
            img = getView().findViewById(R.id.imageViewMeow);
            Picasso.get().load(imageurl).into(img);
        }
        catch (JSONException e) {
            // Hello
        }
    }

    private void PopulateTable() {
        GetJSON();
        /*for (int i=0; i < jsonArray.length(); i++)
        {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
            }
            catch (JSONException e) {
                // Hello
            }
        }*/
    }
}