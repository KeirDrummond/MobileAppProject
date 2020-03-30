package com.example.ratemypet;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

public class GalleryItem {

    public String title = "";
    public String imageURL = "";

    public GalleryItem(String title, String imageURL) {
        this.title = title;
        this.imageURL = imageURL;
    }
}
