package com.example.application.models;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class News {

    @SerializedName("status")
    @Expose
    String status;

    @SerializedName("totalResults")
    @Expose
    int totalResults;

    @SerializedName("articles")
    @Expose
    List<Articles> articles;


    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
    public int getTotalResults() {
        return totalResults;
    }

    public void setArticles(List<Articles> articles) {
        this.articles = articles;
    }
    public List<Articles> getArticles() {
        return articles;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(News.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("status");
        sb.append('=');
        sb.append(((this.status == null)?"<null>":this.status));
        sb.append(',');
        sb.append("totalResults");
        sb.append('=');
        sb.append(((this.totalResults == 0)?0:this.totalResults));
        sb.append(',');
        sb.append("articles");
        sb.append('=');
        sb.append(((this.articles == null)?"<null>":this.articles));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}