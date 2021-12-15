package com.example.listview;

import java.util.Date;

public class FeedBack {
    private long id;  //无需指定
    private String imageUrl; //图片url
    private String title;   //标题
    private String desc;  //描述
    private String account;  //账号
    private String address; //地址，定位
    private String category; //类别：安全隐患、卫生问题、秩序问题
    private int degree;  //级别：0-一般，  1-重要
    private Date time;  //时间: 2021-11-06T13:14:25.909+00:00
    private String process; //当前状态："已提交"

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }
}
