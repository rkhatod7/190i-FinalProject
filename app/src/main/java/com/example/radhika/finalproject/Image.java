package com.example.radhika.finalproject;

import android.graphics.Bitmap;
import android.util.Base64;
import java.io.ByteArrayOutputStream;

/**
 * Created by dretaylo on 6/8/17.
 */

public class Image {
    public String image_id;
    public String bitmap;

    public Image() {
        // keep this empty for firebase
    }

    public Image(String i, Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] byteArrayImage = baos.toByteArray();
        bitmap = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
        image_id = i;
    }

    public String getImageID() {
        return this.image_id;
    }

    public String bitmap() {
        return this.bitmap;
    }
}
