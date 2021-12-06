package com.example.listview;

import java.util.Date;

public class NewsDetails {
    private int id;
    private String imageUrl;
    private String title;
    private String desc;
    private String publishAccount;
    private Date publishTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPublishAccount() {
        return publishAccount;
    }

    public void setPublishAccount(String publishAccount) {
        this.publishAccount = publishAccount;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }
}
