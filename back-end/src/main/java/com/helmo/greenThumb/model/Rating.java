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

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(int dislikeCount) {
        this.dislikeCount = dislikeCount;
    }
}
