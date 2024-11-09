package com.example.recipefinder.model;

import java.io.Serializable;

public class RecipePreview implements Serializable {
    private final int apiId;
    private final String name, imgURL;

    // Constructor
    public RecipePreview(int apiId, String name, String imgURL) {
        this.apiId = apiId;
        this.name = name;
        this.imgURL = imgURL;
    }

    // Getters
    public int getApiId() {
        return apiId;
    }

    public String getImgURL() {
        return imgURL;
    }

    public String getName() {
        return name;
    }
}
