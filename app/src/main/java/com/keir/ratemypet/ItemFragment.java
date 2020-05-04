package com.keir.ratemypet;

import android.media.ExifInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ItemFragment extends Fragment {

    GalleryItem item;

    Button cuteBtn;
    Button funnyBtn;
    Button interestingBtn;
    Button happyBtn;
    Button suprisingBtn;

    Button continueBtn;

    Rating currentRating;
    Rating newRating;

    public static ItemFragment newInstance(GalleryItem item, Rating currentRating) {
        ItemFragment fragment = new ItemFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", item);
        bundle.putSerializable("rating", currentRating);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        this.item = (GalleryItem) getArguments().getSerializable("item");
        currentRating = (Rating) getArguments().getSerializable("rating");
        newRating = new Rating(item.getId());

        TextView textView = view.findViewById(R.id.title);
        ImageView imageView = view.findViewById(R.id.image);

        textView.setText(item.getTitle());
        Glide.with(view).load(item.getImageURL()).into(imageView);

        cuteBtn = view.findViewById(R.id.cuteBtn);
        funnyBtn = view.findViewById(R.id.funnyBtn);
        interestingBtn = view.findViewById(R.id.interestingBtn);
        happyBtn = view.findViewById(R.id.happyBtn);
        suprisingBtn = view.findViewById(R.id.surprisingBtn);

        continueBtn = view.findViewById(R.id.continueBtn);

        cuteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Selection("Cute");
            }
        });
        funnyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Selection("Funny");
            }
        });
        interestingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Selection("Interesting");
            }
        });
        happyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Selection("Happy");
            }
        });
        suprisingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Selection("Surprising");
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Continue();
            }
        });

        return view;
    }

    private void Selection(String selection) {
        switch(selection) {
            case "Cute" :
                if (newRating.getCuteScore() != 0) {
                    newRating.setCuteScore(1);
                }
                else {
                    Deselect("Cute");
                }
                break;
            case "Funny" :
                break;
            case "Interesting" :
                break;
            case "Happy" :
                break;
            case "Suprising" :
                break;
        }
    }

    private void Deselect(String selection) {

    }

    private void Continue() {
        if (!currentRating.equals(newRating)) {
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            final CollectionReference collection = firestore.collection("users")
                    .document(Session.getInstance().getCurrentUser().getUserID())
                    .collection("ratings");

                    collection.whereEqualTo("id", item.getId())
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().getDocuments().size() == 0) {
                            collection.document().set(newRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        ((ItemViewActivity)getActivity()).Continue();
                                    }
                                }
                            });
                        }
                        else {
                            String id = task.getResult().getDocuments().get(0).getId();
                            collection.document(id).set(newRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        ((ItemViewActivity)getActivity()).Continue();
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }
        else {
            ((ItemViewActivity)getActivity()).Continue();
        }
    }
}
