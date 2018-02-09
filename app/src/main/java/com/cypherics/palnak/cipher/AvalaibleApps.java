package com.cypherics.palnak.cipher;

import android.graphics.drawable.Drawable;

/**
 * Created by palnak on 14-1-18.
 */

public class AvalaibleApps {

    int id;

    String name;
    Drawable thumbnail;


    public AvalaibleApps() {
    }

    public AvalaibleApps(String appname, Drawable thumbnails){

        this.name=appname;
        this.thumbnail=thumbnails;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public Drawable getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Drawable thumbnail) {
        this.thumbnail = thumbnail;
    }

}
