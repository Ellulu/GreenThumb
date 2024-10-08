package com.helmo.greenThumb.model;
public class Rating {
    private int like;
    private int dislike;

    public int getAverage() {
        // Implémentation
        return like - dislike;
    }
}

