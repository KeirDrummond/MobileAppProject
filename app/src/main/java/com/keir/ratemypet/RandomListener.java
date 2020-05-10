package com.keir.ratemypet;

import java.util.List;

public interface RandomListener {
    void getResult(List<GalleryItem> items, List<Rating> ratings);
}