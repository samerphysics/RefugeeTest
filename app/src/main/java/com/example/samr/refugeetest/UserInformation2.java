package com.example.samr.refugeetest;

public class UserInformation2 {

    private String request_image_url;
    private String request_description;
    private String request_price;
    private String request_time;
    private String request_title;
    private String request_user_id;

    public UserInformation2() {
    }

    public UserInformation2(String request_image_url, String request_description, String request_price, String request_time, String request_title, String request_user_id) {
        this.request_image_url = request_image_url;
        this.request_description = request_description;
        this.request_price = request_price;
        this.request_time = request_time;
        this.request_title = request_title;
        this.request_user_id = request_user_id;
    }

    public String getRequest_image_url() {
        return request_image_url;
    }

    public void setRequest_image_url(String request_image_url) {
        this.request_image_url = request_image_url;
    }

    public String getRequest_description() {
        return request_description;
    }

    public void setRequest_description(String request_description) {
        this.request_description = request_description;
    }

    public String getRequest_price() {
        return request_price;
    }

    public void setRequest_price(String request_price) {
        this.request_price = request_price;
    }

    public String getRequest_time() {
        return request_time;
    }

    public void setRequest_time(String request_time) {
        this.request_time = request_time;
    }

    public String getRequest_title() {
        return request_title;
    }

    public void setRequest_title(String request_title) {
        this.request_title = request_title;
    }

    public String getRequest_user_id() {
        return request_user_id;
    }

    public void setRequest_user_id(String request_user_id) {
        this.request_user_id = request_user_id;
    }
}
