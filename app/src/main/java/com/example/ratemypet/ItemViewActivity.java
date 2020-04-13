package com.example.ratemypet;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class ItemViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemview);

        RandomItem();
    }

    // Use passed variables to load the correct item.
    // Or load random items.

    public void RandomItem()
    {
        ItemFinder.getInstance().GetRandomItem(new MyListener<GalleryItem>() {
            @Override
            public void getResult(GalleryItem object) {
                // Create a fragment with this object.
                ItemFragment itemFragment = ItemFragment.newInstance(object);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, itemFragment).commit();
            }
        });
    }
}