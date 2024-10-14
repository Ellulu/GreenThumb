package com.helmo.greenThumb.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Rating {

    private int likeCount;
    private int dislikeCount;

    public Rating() {}

    public Rating(int like, int dislike) {
        this.likeCount = like;
        this.dislikeCount = dislike;
    }

    public int getAverage() {
        return likeCount - dislikeCount;
    }

}
