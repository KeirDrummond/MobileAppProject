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

import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        ((MainActivity) getActivity()).ShowTaskbar();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycleitems);

        ItemFinder.getInstance().GetRandomItemList(20, new ListListener<GalleryItem>() {
            @Override
            public void getResult(ArrayList<GalleryItem> itemList) {
                PopulateTable(itemList);
            }
        });

        return view;
    }

    private void PopulateTable(ArrayList<GalleryItem> itemList) {
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(itemList, this.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }
}