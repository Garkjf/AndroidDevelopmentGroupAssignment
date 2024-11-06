package com.example.recipefinder.model;

import java.io.Serializable;

public class RecipePreview implements Serializable {
    private final int recipeId, apiId;
    private final String name, imgURL;

    // Constructor
    public RecipePreview(int recipeId, int apiId, String name, String imgURL) {
        this.recipeId = recipeId;
        this.apiId = apiId;
        this.name = name;
        this.imgURL = imgURL;
    }

    public RecipePreview(int apiId, String name, String imgURL) {
        this.recipeId = 0;
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

    public int getRecipeId() {
        return recipeId;
    }

    public String getName() {
        return name;
    }
}
