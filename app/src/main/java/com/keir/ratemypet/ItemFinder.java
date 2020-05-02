package com.keir.ratemypet;

import android.content.Context;
import android.util.Log;
import android.widget.Gallery;

import androidx.annotation.NonNull;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class ItemFinder {
    // Utility class

    private static ItemFinder instance = null;

    private FirebaseFirestore firestore;

    private ItemFinder(Context context) {
        firestore = FirebaseFirestore.getInstance();
    }

    public static synchronized ItemFinder getInstance(Context context) {
        if (null == instance)
            instance = new ItemFinder(context);
        return instance;
    }

    public static synchronized ItemFinder getInstance()
    {
        return instance;
    }

    public void GetRandomItemList(final int listSize, final ListListener<GalleryItem> listener) {
        firestore.collection("images").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int noOfResults = task.getResult().getDocuments().size();
                            int size = Math.min(listSize, noOfResults);

                            ArrayList<GalleryItem> rndList = new ArrayList<>();
                            int counter = 0;
                            while (counter < size) {
                                if (!rndList.contains(task.getResult().getDocuments().get(counter))) {
                                    rndList.add(new GalleryItem(task.getResult().getDocuments().get(counter)));
                                    counter++;
                                }
                            }
                            listener.getResult(rndList);
                        }
                    }
                });
    }

    public void GetRandomItem(final SingleListener<GalleryItem> listener) {
        firestore.collection("images").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int size = task.getResult().getDocuments().size();
                            if (size != 0) {
                                int rndint = new Random().nextInt(size);
                                GalleryItem item = new GalleryItem(task.getResult().getDocuments().get(rndint));
                                listener.getResult(item);
                            }
                        }
                    }
                });
    }

}
