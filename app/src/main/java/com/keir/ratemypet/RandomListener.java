package com.keir.ratemypet;

import java.util.ArrayList;

public interface RandomListener {
    void getResult(ArrayList<GalleryItem> items, ArrayList<Rating> ratings);
}