package com.keir.ratemypet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private UserAccount user;
    private RecyclerView recyclerView;

    public static ProfileFragment newInstance(UserAccount user) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        user = (UserAccount) getArguments().getSerializable("user");
        TextView usernameDisplay = view.findViewById(R.id.profileUsername);
        usernameDisplay.setText(user.getDisplayName());

        TextView uploadScoreDisplay = view.findViewById(R.id.uploadScore);
        uploadScoreDisplay.setText("Upload Score: " + user.getUploadScore());

        TextView ratingScoreDisplay = view.findViewById(R.id.ratingScore);
        ratingScoreDisplay.setText("Rating Score: " + user.getRatingScore());

        ((MainActivity) getActivity()).ShowTaskbar();

        recyclerView = view.findViewById(R.id.recycleView);

        ItemFinder.getInstance().getProfileItems(user, new GalleryItemListener() {
            @Override
            public void getResult(List<GalleryItem> items) {
                PopulateTable(items);
            }
        });

        return view;
    }

    private void PopulateTable(List<GalleryItem> items) {
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(items, this.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

}
