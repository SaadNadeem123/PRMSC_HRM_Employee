package com.lmkr.prmscemployee.data.webservice.models;

import com.google.gson.annotations.SerializedName;

public class BulletinModel {

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;
    @SerializedName("created_at")
    private String date;
    private String pic_url;

    public BulletinModel(String title, String description, String date, String pic_url) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.pic_url = pic_url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getPic_url() {
        return pic_url;
    }
}
