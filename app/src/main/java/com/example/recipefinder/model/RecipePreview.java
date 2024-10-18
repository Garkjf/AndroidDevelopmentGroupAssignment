package com.example.recipefinder.model;

import java.io.Serializable;

public class RecipePreview implements Serializable {
    private final int recipeId;
    private final String name, imgURL;

    public RecipePreview(int recipeId, String name, String imgURL) {
        this.name = name;
        this.imgURL = imgURL;
        this.recipeId = recipeId;
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
