package com.example.recipefinder.model;

public class RecipePreview {
    private String name, imgURL, id;

    public RecipePreview(String id, String name, String imgURL) {
        this.name = name;
        this.imgURL = imgURL;
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public void setId(String id) {
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
