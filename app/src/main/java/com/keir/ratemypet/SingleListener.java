package com.keir.ratemypet;

import java.util.List;

public interface SingleListener {
    void getResult(List<GalleryItem> item, List<Rating> ratings);
}