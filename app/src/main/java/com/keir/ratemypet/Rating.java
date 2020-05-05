package com.keir.ratemypet;

import java.io.Serializable;

public class Rating implements Serializable {

    private String ratingId;
    private String uploadId;

    public long cuteScore = 0;
    public long funnyScore = 0;
    public long interestingScore = 0;
    public long happyScore = 0;
    public long surprisingScore = 0;

    public Rating() {
    }

    public Rating(String ratingId, String uploadId) {
        this.ratingId = ratingId;
        this.uploadId = uploadId;
    }

    public String getRatingId() { return ratingId; }
    public String getUploadId() { return uploadId; }

}