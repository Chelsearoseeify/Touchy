package com.example.selen.touch.helper;

/**
 * Created by Lincoln on 18/05/16.
 */
public class CategoryCard {
    private String name;
    private int thumbnail;


    public CategoryCard(String name, int thumbnail) {
        this.name = name;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getThumbnail() {
        return thumbnail;
    }
}
