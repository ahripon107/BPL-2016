package com.banglacricket.bdcricketteam.model;

import android.graphics.Bitmap;

import com.google.inject.Singleton;

/**
 * @author Ripon
 */
@Singleton
public class ImageHolder {

    private Bitmap image;

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
