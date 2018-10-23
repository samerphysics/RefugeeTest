package com.example.samr.refugeetest;

import com.google.firebase.database.DataSnapshot;

public class PojoRequest {
    private String requestId;
    private String userId;
    private String title;
    private String description;
    private String price;
    private String time;
    private String imageUrl;

    public PojoRequest() {
    }

    public PojoRequest(DataSnapshot ds) {
        this.requestId = ds.getKey();
        this.userId = ds.child("request_user_id").getValue().toString();
        this.title = ds.child("request_title").getValue().toString();
        this.description = ds.child("request_description").getValue().toString();
        this.price = ds.child("request_price").getValue().toString();
        this.time = ds.child("request_time").getValue().toString();
        this.imageUrl = ds.child("request_image_url").getValue().toString();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
