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

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ((MainActivity) getActivity()).ShowTaskbar();

        Button uploadBtn = view.findViewById(R.id.uploadbtn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).ChangeFragment(new FileUploadFragment());
            }
        });

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
        ItemFinder.getInstance().GetRandomItemList(5, new RandomListener() {
            @Override
            public void getResult(ArrayList<GalleryItem> items, ArrayList<Rating> ratings) {
                if (items.size() != 0) {
                    Intent intent = new Intent(getContext(), ItemViewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("items", items);
                    bundle.putSerializable("ratings", ratings);
                    intent.putExtra("items", bundle);

                    startActivity(intent);
                }
            }
        });
    }

}
