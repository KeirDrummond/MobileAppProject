package com.keir.ratemypet;

import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class GalleryItem implements Serializable {

    private String title = "";
    private String imageURL = "";

    public GalleryItem(DocumentSnapshot doc)
    {
        Map<String, Object> map = Validate(doc.getData());
        title = map.get("title").toString();
        imageURL = map.get("url").toString();
    }

    public GalleryItem(String title, String imageURL) {
        this.title = title;
        this.imageURL = imageURL;
    }

    private Map<String, Object> Validate(Map<String, Object> map) {
        Map<String, Object> newMap = map;
        if (!newMap.containsKey("title")) { newMap.put("title", "Title"); }
        if (!newMap.containsKey("url")) { newMap.put("url", ""); }

        return newMap;
    }

    public String getTitle() {
        return title;
    }

    public String getImageURL() {
        return imageURL;
    }
}
