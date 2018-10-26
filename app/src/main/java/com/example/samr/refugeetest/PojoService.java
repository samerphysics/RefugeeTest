package com.example.samr.refugeetest;

import com.google.firebase.database.DataSnapshot;

public class PojoService {
    private String serviceId;
    private String userId;
    private String title;
    private String description;
    private String price;
    private String time;
    private String imageUrl;

    public PojoService() {
    }

    public PojoService(DataSnapshot ds) {
        this.serviceId = ds.getKey();
        this.userId = ds.child("service_user_id").getValue().toString();
        this.title = ds.child("service_title").getValue().toString();
        this.description = ds.child("service_description").getValue().toString();
        this.price = ds.child("service_price").getValue().toString();
        this.time = ds.child("service_time").getValue().toString();
        this.imageUrl = ds.child("service_image_url").getValue().toString();
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

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
