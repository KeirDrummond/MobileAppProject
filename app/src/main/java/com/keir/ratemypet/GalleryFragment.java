package com.keir.ratemypet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

public class GalleryFragment extends Fragment {

    private SearchView searchView;
    RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar loadingOverlay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        ((MainActivity) getActivity()).TaskbarDisplay(true);

        searchView = (SearchView) view.findViewById(R.id.search_bar);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleitems);
        loadingOverlay = (ProgressBar) view.findViewById(R.id.loading);

        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        loadingOverlay.setVisibility(View.VISIBLE);
        Query query = firestore.collection("images").limit(50);
        ItemFinder.getInstance().GetGalleryItems(query, new GalleryItemListener() {
            @Override
            public void getResult(List<GalleryItem> items) {
                PopulateTable(items);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String search = searchView.getQuery().toString().trim();
                Query query = firestore.collection("images").whereEqualTo("title", search);
                loadingOverlay.setVisibility(View.VISIBLE);
                if (adapter != null) { adapter.clearList(); }
                ItemFinder.getInstance().GetGalleryItems(query, new GalleryItemListener() {
                    @Override
                    public void getResult(List<GalleryItem> items) {
                        PopulateTable(items);
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return view;
    }

    private void PopulateTable(List<GalleryItem> itemList) {
        loadingOverlay.setVisibility(View.INVISIBLE);
        adapter = new RecyclerViewAdapter(itemList, this.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }
}