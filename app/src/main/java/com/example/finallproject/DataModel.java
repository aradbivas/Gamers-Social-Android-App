package com.example.finallproject;


public class DataModel {

    String name;

    int id_;
    int image;

    public DataModel(String name, int id_, int image) {
        this.name = name;
        this.id_ = id_;
        this.image=image;
    }
    public DataModel(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }


    public int getImage() {
        return image;
    }

    public int getId() {
        return id_;
    }
}