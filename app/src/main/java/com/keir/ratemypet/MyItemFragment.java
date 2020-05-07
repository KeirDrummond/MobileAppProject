package com.keir.ratemypet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyItemFragment extends ItemFragment {

    GalleryItem item;

    long cuteScore;
    long funnyScore;
    long interestingScore;
    long happyScore;
    long surprisingScore;
    long totalScore;

    TextView cuteScoreText;
    TextView funnyScoreText;
    TextView interestingScoreText;
    TextView happyScoreText;
    TextView surprisingScoreText;
    TextView totalScoreText;

    Button continueBtn;

    public static GenericItemFragment newInstance(GalleryItem item) {
        GenericItemFragment fragment = new GenericItemFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", item);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myitem, container, false);

        continueBtn = view.findViewById(R.id.continueBtn);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ItemViewActivity) getActivity()).Continue();
            }
        });

        return view;
    }
}
