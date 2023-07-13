package com.example.application.models;
import java.util.Date;

import com.google.gson.annotations.SerializedName;


public class Articles {

    @SerializedName("source")
    Source source;

    @SerializedName("author")
    String author;

    @SerializedName("title")
    String title;

    @SerializedName("description")
    String description;

    @SerializedName("url")
    String url;

    @SerializedName("urlToImage")
    String urlToImage;

    @SerializedName("publishedAt")
    Date publishedAt;

    @SerializedName("content")
    String content;


    public void setSource(Source source) {
        this.source = source;
    }
    public Source  getSource() {
        return source;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    public String getAuthor() {
        return author;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return url;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }
    public String getUrlToImage() {
        return urlToImage;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }
    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Articles.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("source");
        sb.append('=');
        sb.append(((this.source == null)?"<null>":this.source));
        sb.append(',');
        sb.append("author");
        sb.append('=');
        sb.append(((this.author == null)?"<null>":this.author));
        sb.append(',');
        sb.append("title");
        sb.append('=');
        sb.append(((this.title == null)?"<null>":this.title));
        sb.append(',');
        sb.append("description");
        sb.append('=');
        sb.append(((this.description == null)?"<null>":this.description));
        sb.append(',');
        sb.append("url");
        sb.append('=');
        sb.append(((this.url == null)?"<null>":this.url));
        sb.append(',');
        sb.append("urlToImage");
        sb.append('=');
        sb.append(((this.urlToImage == null)?"<null>":this.urlToImage));
        sb.append(',');
        sb.append("publishedAt");
        sb.append('=');
        sb.append(((this.publishedAt == null)?"<null>":this.publishedAt));
        sb.append(',');
        sb.append("content");
        sb.append('=');
        sb.append(((this.content == null)?"<null>":this.content));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }


}