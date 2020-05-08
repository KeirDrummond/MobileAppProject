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
        final CollectionReference ratingsReference = firestore.collection("users")
                .document(Session.getInstance().getCurrentUser().getUserID())
                .collection("ratings");

        itemRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                final GalleryItem newItem = documentSnapshot.toObject(GalleryItem.class);

                ratingsReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Rating rating;
                        String uploadId = item.getId();
                        String ratingId = ratingsReference.document().getId();
                        rating = new Rating(ratingId, uploadId, item.getUploaderId());

                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            String ratingUId = doc.get("uploadId").toString();
                            if (ratingUId.equals(uploadId)) {
                                rating = (doc.toObject(Rating.class));
                                break;
                            }
                        }
                        ArrayList<GalleryItem> aItem = new ArrayList();
                        aItem.add(newItem);
                        ArrayList<Rating> aRating = new ArrayList();
                        aRating.add(rating);
                        listener.getResult(aItem, aRating);
                    }
                });
            }
        });
    }

    public void GetRandomItemList(final int listSize, final RandomListener listener) {
        firestore.collection("images")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                        int noOfResults = docs.size();
                        int size = Math.min(listSize, noOfResults);

                        final ArrayList<GalleryItem> rndList = new ArrayList<>();
                        final ArrayList<Rating> ratings = new ArrayList<>();

                        int counter = 0;
                        while (counter < size) {
                            int randomValue = new Random().nextInt(noOfResults);
                            if (rndList.size() != 0) {
                                boolean copy = false;
                                for (GalleryItem item : rndList) {
                                    String itemId = item.getId();
                                    String otherItemId = docs.get(randomValue).getId();
                                    if (itemId.equals(otherItemId)) {
                                        copy = true;
                                        break;
                                    }
                                }
                                if (!copy) {
                                    rndList.add(docs.get(randomValue).toObject(GalleryItem.class));
                                    counter++;
                                }
                            } else {
                                rndList.add(docs.get(randomValue).toObject(GalleryItem.class));
                                counter++;
                            }
                        }

                        final CollectionReference ratingRef = firestore.collection("users")
                                .document(Session.getInstance().getCurrentUser().getUserID())
                                .collection("ratings");
                        ratingRef.get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for (int i = 0; i < rndList.size(); i++) {
                                            String uploadId = rndList.get(i).getId();
                                            String ratingId = ratingRef.document().getId();

                                            Rating rating = new Rating(ratingId, uploadId, rndList.get(i).getUploaderId());
                                            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                                                String ratingUId = doc.get("uploadId").toString();
                                                if (ratingUId.equals(uploadId)) {
                                                    rating = doc.toObject(Rating.class);
                                                    break;
                                                }
                                            }
                                            ratings.add(rating);
                                        }
                                        listener.getResult(rndList, ratings);
                                    }
                                });
                    }
                });
    }

    public void getProfileItems(final UserAccount user, final GalleryItemListener listener) {
        Query query = firestore.collection("images").whereEqualTo("uploaderId", user.getUserID());
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
