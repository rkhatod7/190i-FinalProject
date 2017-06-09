package com.example.radhika.finalproject;

/**
 * Created by dretaylo on 6/8/17.
 */

public class ImagePost {
    public String image_id;
    public String place_id;

    public ImagePost() {
        // keep this empty for firebase
    }

    public ImagePost(String i, String p) {
        image_id = i;
        place_id = p;
    }

    public String getImageID() {
        return this.image_id;
    }

    public String getPlaceID() {
        return this.place_id;
    }
}
