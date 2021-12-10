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
}
