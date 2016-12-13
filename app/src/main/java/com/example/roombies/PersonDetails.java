package com.example.roombies;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class PersonDetails {
    String name;
    String place;
    String city;
    Bitmap img;
    Bitmap dp;
    Integer price;

    public PersonDetails(String name,String place,String city,Integer price,Bitmap img,Bitmap dp) {
        this.name = name;
        this.place = place;
        this.city = city;
        this.img = img;
        this.dp = dp;
        this.price = price;
    }

    public String getName() {
        return this.name;
    }
    public String getPlace() {
        return this.place;
    }
    public String getCity() {
        return this.city;
    }
    public Bitmap getImg(){
        return this.img;
    }
    public Bitmap getDp(){
        return this.dp;
    }
    public Integer getPrice(){
        return this.price;
    }
}
