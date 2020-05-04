package com.keir.ratemypet;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

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

    public void GetGalleryItems(Query query, final GalleryItemListener listener) {
        query.limit(50).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<GalleryItem> items = new ArrayList<>();

                            for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                items.add(doc.toObject(GalleryItem.class));
                            }
                            listener.getResult(items);
                        }
                    }
                });
    }

    public void GetMyRatings(final ArrayList<GalleryItem> items, final RatingListener listener) {
        firestore.collection("users")
                .document(Session.getInstance().getCurrentUser().getUserID())
                .collection("ratings")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        final ArrayList<Rating> ratings = new ArrayList<>();
                        for (int i = 0; i < items.size(); i++) {
                            String id = items.get(i).getId();
                            for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                if (doc.get("id") == id) {
                                    ratings.add(doc.toObject(Rating.class));
                                    break;
                                }
                            }
                            ratings.add(new Rating());
                        }
                        listener.getResult(ratings);
                    }
                });
    }

    public void GetRandomItemList(final int listSize, final RandomListener listener) {
        firestore.collection("images")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int noOfResults = task.getResult().getDocuments().size();
                            int size = Math.min(listSize, noOfResults);

                            final ArrayList<GalleryItem> rndList = new ArrayList<>();
                            final ArrayList<Rating> ratings = new ArrayList<>();

                            int counter = 0;
                            while (counter < size) {
                                if (!rndList.contains(task.getResult().getDocuments().get(counter))) {
                                    rndList.add(task.getResult().getDocuments().get(counter).toObject(GalleryItem.class));
                                    counter++;
                                }
                            }

                            firestore.collection("users")
                                    .document(Session.getInstance().getCurrentUser().getUserID())
                                    .collection("ratings")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (int i = 0; i < rndList.size(); i++) {
                                                    String id = rndList.get(i).getId();
                                                    for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                                        if (doc.get("id") == id) {
                                                            ratings.add(doc.toObject(Rating.class));
                                                            break;
                                                        }
                                                    }
                                                    ratings.add(new Rating());
                                                }
                                                listener.getResult(rndList, ratings);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}
