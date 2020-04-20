package com.keir.ratemypet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenItemActivity();
            }
        });

        return view;
    }

    public void OpenItemActivity() {
        ItemFinder.getInstance().GetRandomItemList(5, new ListListener<GalleryItem>() {
            @Override
            public void getResult(ArrayList<GalleryItem> objectList) {
                Intent intent = new Intent(getContext(), ItemViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("itemList", (Serializable)objectList);
                intent.putExtra("itemList", bundle);

                startActivity(intent);
            }
        });
    }

}
