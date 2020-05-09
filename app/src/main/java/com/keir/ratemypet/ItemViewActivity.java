package com.keir.ratemypet;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ItemViewActivity extends AppCompatActivity {

    ArrayList<ItemFragment> itemFragments;
    int currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemview);

        Intent intent = getIntent();

        Bundle bundle = intent.getBundleExtra("items");
        ArrayList<GalleryItem> itemList = (ArrayList<GalleryItem>)bundle.getSerializable("items");
        ArrayList<Rating> ratings = (ArrayList<Rating>)bundle.getSerializable("ratings");
        itemFragments = new ArrayList<>();

        for (int i = 0; i < itemList.size(); i++)
        {
            String uploaderId = itemList.get(i).getUploaderId();
            if (!uploaderId.equals(Session.getInstance().getCurrentUser().getUserId())) {
                GenericItemFragment genericItemFragment = GenericItemFragment.newInstance(itemList.get(i), ratings.get(i));
                itemFragments.add(genericItemFragment);
            }
            else {
                MyItemFragment myItemFragment = MyItemFragment.newInstance(itemList.get(i));
                itemFragments.add(myItemFragment);
            }

        }
        currentItem = 0;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, itemFragments.get(currentItem)).commit();
    }

    public void Continue() {
        if (currentItem + 1 != itemFragments.size()) {
            currentItem++;
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, itemFragments.get(currentItem)).commit();
        }
        else {
            finish();
        }
    }
}