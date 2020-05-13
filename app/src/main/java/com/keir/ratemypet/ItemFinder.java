package com.keir.ratemypet;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    public void GetItem(final GalleryItem item, final SingleListener listener) {
        final DocumentReference itemRef = firestore.collection("images").
                document(item.getId());
        final CollectionReference userRatingsReference = firestore.collection("users")
                .document(Session.getInstance().getCurrentUser().getUserId())
                .collection("ratings");

        itemRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                final GalleryItem newItem = documentSnapshot.toObject(GalleryItem.class);

                userRatingsReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String uploadId = item.getId();

                        boolean found = false;
                        String foundRatingId = "";

                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            String ratingUId = doc.get("uploadId").toString();
                            if (ratingUId.equals(uploadId)) {
                                found = true;
                                foundRatingId = doc.get("ratingId").toString();
                                break;
                            }
                        }

                        final ArrayList<GalleryItem> aItem = new ArrayList();
                        aItem.add(newItem);

                        if (found) {
                            firestore.collection("images").document(item.getId()).collection("ratings").document(foundRatingId).get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            List<Rating> aRating = new ArrayList<>();
                                            Rating rating = documentSnapshot.toObject(Rating.class);
                                            aRating.add(rating);
                                            listener.getResult(aItem, aRating);
                                        }
                                    });
                        } else {
                            ArrayList<Rating> aRating = new ArrayList();
                            String ratingId = userRatingsReference.document().getId();
                            Rating rating = new Rating(ratingId, uploadId, item.getUploaderId());
                            aRating.add(rating);
                            listener.getResult(aItem, aRating);
                        }


                    }
                });
            }
        });
    }

    /*
    // Get a random list of items.
    // The item list follows some restrictions.
    // An item will not appear in the list more than once.
    // An item will not appear if the user has rated it before.
    // An item will not appear if it is their own.
    */

    public void GetRandomItemList(final int listSize, final RandomListener listener) {

        firestore.collection("images").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                final List<DocumentSnapshot> images = queryDocumentSnapshots.getDocuments();

                firestore.collection("users").document(Session.getInstance().getCurrentUser().getUserId())
                        .collection("ratings").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> myRatings = queryDocumentSnapshots.getDocuments();

                        final List<GalleryItem> randomList = new ArrayList<>(); // The list object that we will be returning.
                        final List<Rating> ratings = new ArrayList<>(); // The associated ratings for each item.

                        for (DocumentSnapshot image : images) {
                            boolean valid = true;
                            String id = image.get("id").toString();
                            if (valid) {                                                            // Check if the item is not belong to the user.
                                String uploaderId = image.get("uploaderId").toString();
                                String userId = Session.getInstance().getCurrentUser().getUserId();
                                if (uploaderId.equals(userId)) {
                                    valid = false;
                                }
                            }
                            if (valid) {                                                            // Check if the item has not previously been rated.
                                for (DocumentSnapshot rating : myRatings) {
                                    String uploadId = rating.get("uploadId").toString();
                                    if (uploadId.equals(id)) {
                                        valid = false;
                                        break;
                                    }
                                }
                            }
                            if (valid) {
                                GalleryItem item = image.toObject(GalleryItem.class);
                                randomList.add(item);
                            }
                        }
                        Collections.shuffle(randomList);

                        int size = Math.min(listSize, randomList.size()); // Ensures the final list size is not larger than the amount of results.

                        List<GalleryItem> items = new ArrayList<>();
                        if (randomList.size() != 0) {
                            for (int i = 0; i < size; i++) {
                                items.add(i, randomList.get(i));
                            }
                        }

                        for (GalleryItem item : items) {
                            String newRatingId = firestore.collection("images").document(item.getId()).collection("ratings").document().getId();
                            Rating rating = new Rating(newRatingId, item.getId(), Session.getInstance().getCurrentUser().getUserId());
                            ratings.add(rating);
                        }
                        listener.getResult(items, ratings);
                    }
                });
            }
        });
    }

    public void getProfileItems(final UserAccount user, final GalleryItemListener listener) {
        Query query = firestore.collection("images").whereEqualTo("uploaderId", user.getUserId());
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<GalleryItem> items = new ArrayList<>();
                for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                    items.add(doc.toObject(GalleryItem.class));
                }
                listener.getResult(items);
            }
        });
    }

}
