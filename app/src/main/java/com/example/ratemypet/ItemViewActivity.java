package com.example.ratemypet;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ItemViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemview);

        Intent intent = getIntent();
        if (intent.getBundleExtra("item") != null)
        {
            Bundle bundle = intent.getBundleExtra("item");
            GalleryItem item = (GalleryItem)bundle.getSerializable("item");

            ItemFragment itemFragment = ItemFragment.newInstance(item);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, itemFragment).commit();
        }
        else if (intent.getBundleExtra("itemList") != null)
        {
            Bundle bundle = intent.getBundleExtra("itemList");
            ArrayList<GalleryItem> itemList = (ArrayList<GalleryItem>)bundle.getSerializable("itemList");
            ArrayList<ItemFragment> itemFragments = new ArrayList<ItemFragment>();
            for (int i = 0; i < itemList.size(); i++)
            {
                ItemFragment itemFragment = ItemFragment.newInstance(itemList.get(i));
                itemFragments.add(itemFragment);
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, itemFragments.get(0)).commit();
        }

    }
}