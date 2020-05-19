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

import java.util.List;

public class ProfileFragment extends Fragment {

    private UserAccount user;
    private RecyclerView recyclerView;

    TextView usernameDisplay;
    TextView uploadScoreDisplay;
    TextView ratingScoreDisplay;

    public static ProfileFragment newInstance(String userId) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        usernameDisplay = view.findViewById(R.id.profileUsername);
        uploadScoreDisplay = view.findViewById(R.id.uploadScore);
        ratingScoreDisplay = view.findViewById(R.id.ratingScore);

        recyclerView = view.findViewById(R.id.recycleView);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        ((MainActivity) getActivity()).TaskbarDisplay(true);
        String userId = getArguments().getString("userId");

        ((MainActivity) getActivity()).Loading(true);
        ItemFinder.getInstance().getUser(userId, new UserListener() {
            @Override
            public void getResult(UserAccount user) {
                SetupProfile(user);
                ((MainActivity) getActivity()).Loading(false);
            }
        });
    }

    private void SetupProfile(UserAccount user) {
        this.user = user;
        usernameDisplay.setText(user.getDisplayName());
        uploadScoreDisplay.setText(Long.toString(user.getUploadScore()));
        ratingScoreDisplay.setText(Long.toString(user.getRatingScore()));

        ItemFinder.getInstance().getProfileItems(user, new GalleryItemListener() {
            @Override
            public void getResult(List<GalleryItem> items) {
                PopulateTable(items);
            }
        });
    }

    private void PopulateTable(List<GalleryItem> items) {
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(items, this.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

}
