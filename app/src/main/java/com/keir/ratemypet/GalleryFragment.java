package com.keir.ratemypet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        ((MainActivity) getActivity()).ShowTaskbar();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycleitems);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        Query query = firestore.collection("images");
        ItemFinder.getInstance().GetGalleryItems(query, new GalleryItemListener() {
            @Override
            public void getResult(List<GalleryItem> items) {
                PopulateTable(items);
            }
        });

        return view;
    }

    private void PopulateTable(List<GalleryItem> itemList) {
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(itemList, this.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }
}