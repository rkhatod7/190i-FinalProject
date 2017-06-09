package com.example.radhika.finalproject;

/**
 * Created by dretaylo on 6/8/17.
 */

public class PlaceDetail {
    public String rating;
    public String count;
    public String comment;

    public PlaceDetail() {
        // keep this empty for firebase
    }

    public PlaceDetail(String rating, String count, String comment) {
        this.rating = rating;
        this.count = count;
        this.comment = comment;
    }

    public String getRating() {
        return this.rating;
    }

    public String getCount() {
        return this.count;
    }

    public String getComment() {
        return this.comment;
    }
}
