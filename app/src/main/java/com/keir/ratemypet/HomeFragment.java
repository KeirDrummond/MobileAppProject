package com.keir.ratemypet;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.Serializable;
import java.util.List;

public class HomeFragment extends Fragment {

    GoogleSignInClient googleSignInClient;
    LinearLayout itemLayout;
    ProgressBar loadingOverlay;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getContext(), googleSignInOptions);

        itemLayout = view.findViewById(R.id.itemLayout);
        loadingOverlay = view.findViewById(R.id.loading);

        ((MainActivity) getActivity()).TaskbarDisplay(true);

        Button button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenItemActivity();
            }
        });

        Button signOutButton = view.findViewById(R.id.signOut);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignInClient.signOut();
                FirebaseAuth.getInstance().signOut();
            }
        });

        loadingOverlay.setVisibility(View.VISIBLE);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        Query query = firestore.collection("images").orderBy("totalScore").limit(2);
        ItemFinder.getInstance().GetGalleryItems(query, new GalleryItemListener() {
            @Override
            public void getResult(List<GalleryItem> items) {
                if (items.size() >= 1) {
                    FrameLayout frame1 = view.findViewById(R.id.item1);
                    FillFrame(frame1, items.get(0), inflater, container);
                }
                if (items.size() == 2) {
                    FrameLayout frame2 = view.findViewById(R.id.item2);
                    FillFrame(frame2, items.get(1), inflater, container);
                }

                loadingOverlay.setVisibility(View.INVISIBLE);
            }
        });

        return view;
    }

    private void FillFrame(FrameLayout frame, final GalleryItem item, LayoutInflater inflater, ViewGroup container) {
        View itemView = inflater.inflate(R.layout.gallery_item, container, false);

        ViewGroup.LayoutParams params = itemView.getLayoutParams();
        params.height = ActionBar.LayoutParams.MATCH_PARENT;
        itemView.setLayoutParams(params);

        ImageView thumbnail = itemView.findViewById(R.id.thumbnail);
        TextView title = itemView.findViewById(R.id.title);
        TextView user = itemView.findViewById(R.id.username);
        Glide.with(getContext()).load(item.getImageURL()).into(thumbnail);
        title.setText(item.getTitle());
        user.setText(item.getUploaderName());
        frame.addView(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getContext()).Loading(true);
                ItemFinder.getInstance().GetItem(item, new SingleListener() {
                    @Override
                    public void getResult(List<GalleryItem> item, List<Rating> rating) {
                        Intent intent = new Intent(getContext(), ItemViewActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("items", (Serializable) item);
                        bundle.putSerializable("ratings", (Serializable) rating);
                        intent.putExtra("items", bundle);

                        ((MainActivity) getContext()).OpenItems(intent);
                    }
                });
            }
        });
    }

    public void OpenItemActivity() {
        ((MainActivity) getActivity()).Loading(true);
        ItemFinder.getInstance().GetRandomItemList(5, new RandomListener() {
            @Override
            public void getResult(List<GalleryItem> items, List<Rating> ratings) {
                if (items.size() != 0) {
                    Intent intent = new Intent(getContext(), ItemViewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("items", (Serializable) items);
                    bundle.putSerializable("ratings", (Serializable) ratings);
                    intent.putExtra("items", bundle);

                    ((MainActivity) getActivity()).OpenItems(intent);
                } else {
                    ((MainActivity) getActivity()).Loading(false);
                    Toast toast = Toast.makeText(getContext(), "Unable to find unrated pets.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

}
