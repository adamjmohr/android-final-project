package com.example.androidlabs;


import java.io.Serializable;

public class News_item implements Serializable {
    private long news_id;
    private String news_title;
    private String news_description;
    private String news_url;
    private String news_imageUrl;

    public News_item() {
    }

    public News_item(long news_id, String news_title, String news_description, String news_url, String news_imageUrl) {
        this.news_id = news_id;
        this.news_title = news_title;
        this.news_description = news_description;
        this.news_url = news_url;
        this.news_imageUrl = news_imageUrl;
    }

    public long getNews_id() {
        return news_id;
    }

    public void setNews_id(long news_id) {
        this.news_id = news_id;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public String getNews_description() {
        return news_description;
    }

    public void setNews_description(String news_description) {
        this.news_description = news_description;
    }

    public String getNews_url() {
        return news_url;
    }

    public void setNews_url(String news_url) {
        this.news_url = news_url;
    }

    public String getNews_imageUrl() {
        return news_imageUrl;
    }

    public void setNews_imageUrl(String news_imageUrl) {
        this.news_imageUrl = news_imageUrl;
    }
}
