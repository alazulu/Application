package com.example.application.models;

import android.graphics.Bitmap;

public class NewsItem {
    private String title;
    private String content;
    private Bitmap image;
    private String url;


    public void setItemTitle(String title) {
        this.title = title;
    }
    public String getItemTitle() {
        return title;
    }

    public void setItemContent(String content) {
        this.content = content;
    }
    public String getItemContent() {
        return content;
    }

    public void setItemImage(Bitmap image) {
        this.image = image;
    }
    public Bitmap getItemImage() {
        return image;
    }

    public void setItemUrl(String url) {
        this.url = url;
    }
    public String getItemUrl() {
        return url;
    }




}
