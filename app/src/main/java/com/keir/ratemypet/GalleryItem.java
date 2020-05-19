package com.keir.ratemypet;

import com.google.firebase.Timestamp;
import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class GalleryItem implements Serializable {

    private String id = "";
    private String title = "";
    private String imageId = "";
    private String imageURL = "";

    private String uploaderId;
    @Exclude
    private String uploaderName;
    private Timestamp timestamp;

    private long cuteScore;
    private long funnyScore;
    private long interestingScore;
    private long happyScore;
    private long surprisingScore;

    private long totalScore;

    public GalleryItem() {
    }

    public GalleryItem(String id, String title, String imageId, String imageURL, String uploaderId, String uploaderName, Timestamp timestamp) {
        this.id = id;
        this.title = title;
        this.imageId = imageId;
        this.imageURL = imageURL;
        this.uploaderId = uploaderId;
        this.uploaderName = uploaderName;
        this.timestamp = timestamp;

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

    public String getImageId() { return imageId; }

    public String getImageURL() {
        return imageURL;
    }

    public String getUploaderId() {
        return uploaderId;
    }

    public String getUploaderName() {
        if (!uploaderName.isEmpty()) { return uploaderName; } return "Username"; }

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
