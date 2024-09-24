package com.example.recipefinder;

public class RecipePreview {
    String name, imgURL, id, desc;

    public RecipePreview(String id, String name, String imgURL, String desc) {
        this.name = name;
        this.imgURL = imgURL;
        this.id = id;
        this.desc = desc;
    }
}
