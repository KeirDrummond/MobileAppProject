package com.keir.ratemypet;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ItemViewActivity extends AppCompatActivity {

    ArrayList<GenericItemFragment> genericItemFragments;
    int currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemview);

        Intent intent = getIntent();

        Bundle bundle = intent.getBundleExtra("items");
        ArrayList<GalleryItem> itemList = (ArrayList<GalleryItem>)bundle.getSerializable("items");
        ArrayList<Rating> ratings = (ArrayList<Rating>)bundle.getSerializable("ratings");
        genericItemFragments = new ArrayList<>();

        for (int i = 0; i < itemList.size(); i++)
        {
            GenericItemFragment genericItemFragment = GenericItemFragment.newInstance(itemList.get(i), ratings.get(i));
            genericItemFragments.add(genericItemFragment);
        }
        currentItem = 0;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, genericItemFragments.get(currentItem)).commit();
    }

    public void Continue() {
        if (currentItem + 1 != genericItemFragments.size()) {
            currentItem++;
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, genericItemFragments.get(currentItem)).commit();
        }
        else {
            finish();
        }
    }
}