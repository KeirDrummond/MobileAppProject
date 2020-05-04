package com.keir.ratemypet;

import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;

public class GalleryItem implements Serializable {

    private String id = "";
    private String title = "";
    private String imageURL = "";

    private String uploaderId;
    private String uploadDate;
    private String uploadTime;

    private long cuteScore;
    private long funnyScore;
    private long interestingScore;
    private long happyScore;
    private long surprisingScore;

    private long totalScore;

    public GalleryItem() {
    }

    public GalleryItem(String id, String title, String imageURL, String uploaderId, String uploadDate, String uploadTime) {
        this.id = id;
        this.title = title;
        this.imageURL = imageURL;
        this.uploaderId = uploaderId;
        this.uploadDate = uploadDate;
        this.uploadTime = uploadTime;

        cuteScore = 0;
        funnyScore = 0;
        interestingScore = 0;
        happyScore = 0;
        surprisingScore = 0;
        totalScore = 0;
    }

    public String getId() { return id; }

    public String getTitle() {
        return title;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getUploaderId() {
        return uploaderId;
    }

    public long getCuteScore() {
        return cuteScore;
    }

    public long getFunnyScore() {
        return funnyScore;
    }

    public long getInterestingScore() {
        return interestingScore;
    }

    public long getHappyScore() {
        return happyScore;
    }

    public long getSurprisingScore() {
        return surprisingScore;
    }

    public long getTotalScore() {
        return totalScore;
    }
}
