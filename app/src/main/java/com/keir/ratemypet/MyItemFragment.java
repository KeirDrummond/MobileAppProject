package com.keir.ratemypet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MyItemFragment extends ItemFragment {

    GalleryItem item;

    public static MyItemFragment newInstance(GalleryItem item) {
        MyItemFragment fragment = new MyItemFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", item);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myitem, container, false);

        this.item = (GalleryItem) getArguments().getSerializable("item");

        TextView textView = view.findViewById(R.id.title);
        ImageView imageView = view.findViewById(R.id.image);

        textView.setText(item.getTitle());
        Glide.with(view).load(item.getImageURL()).into(imageView);

        TextView cuteScoreText = view.findViewById(R.id.cuteScore);
        TextView funnyScoreText = view.findViewById(R.id.funnyScore);
        TextView interestingScoreText = view.findViewById(R.id.interestingScore);
        TextView happyScoreText = view.findViewById(R.id.happyScore);
        TextView surprisingScoreText = view.findViewById(R.id.surprisingScore);
        TextView totalScoreText = view.findViewById(R.id.totalScore);

        cuteScoreText.setText(String.valueOf(item.getCuteScore()));
        funnyScoreText.setText(String.valueOf(item.getFunnyScore()));
        interestingScoreText.setText(String.valueOf(item.getInterestingScore()));
        happyScoreText.setText(String.valueOf(item.getHappyScore()));
        surprisingScoreText.setText(String.valueOf(item.getSurprisingScore()));
        totalScoreText.setText(String.valueOf(item.getTotalScore()));

        Button deleteBtn = view.findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteItem();
            }
        });

        Button continueBtn = view.findViewById(R.id.continueBtn);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ItemViewActivity) getActivity()).Continue();
            }
        });

        return view;
    }

    private void DeleteItem() {
        String UserId = Session.getInstance().getCurrentUser().getUserId();
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/" + UserId + "/" + item.getImageId());

        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        final DocumentReference itemDocRef = firestore.collection("images").document(item.getId());
        final CollectionReference ratingsRef = itemDocRef.collection("ratings");

        ratingsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(final QuerySnapshot queryDocumentSnapshots) {
                firestore.runTransaction(new Transaction.Function<Void>() {
                    @Nullable
                    @Override
                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            String uploaderId = doc.get("uploaderId").toString();
                            String ratingId = doc.get("ratingId").toString();
                            DocumentReference userRatingRef = firestore.collection("users").document(uploaderId).collection("ratings").document(ratingId);
                            transaction.delete(userRatingRef);
                        }

                        transaction.delete(itemDocRef);
                        storageReference.delete();
                        return null;
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        ((ItemViewActivity) getActivity()).Continue();
                    }
                });
            }
        });
    }

}
