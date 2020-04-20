package com.keir.ratemypet;

import java.io.Serializable;

public class GalleryItem implements Serializable {

    public String title = "";
    public String imageURL = "";

    public GalleryItem(String title, String imageURL) {
        this.title = title;
        this.imageURL = imageURL;
    }
}
