package com.example.recipefinder.model;

public class RecipePreview {
    private final String name, imgURL, id;

    public RecipePreview(String id, String name, String imgURL) {
        this.name = name;
        this.imgURL = imgURL;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getImgURL() {
        return imgURL;
    }

    public String getId() {
        return id;
    }
}
