package com.example.cafetotravel;

import android.app.Application;

public class Data extends Application {

    private String user_id;
    private String title;
    private String title_insta;
    private String address;
    private String imageUrl;
    private String callNum;
    private String hour;
    private String content;
    //private boolean isChecked;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) { this.user_id = user_id; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleInsta() {
        return title_insta;
    }

    public void setTitleInsta(String title_insta) {
        this.title_insta = title_insta;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() { return imageUrl;    }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getCallNum() { return callNum; }

    public void setCallNum(String callNum) { this.callNum = callNum; }

    public String getHour() { return hour; }

    public void setHour(String hour) { this.hour = hour; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    //public boolean isChecked() { return isChecked; }

    //public void setChecked(boolean isChecked) { this.isChecked = isChecked; }
}
