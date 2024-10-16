package com.example.recipefinder.model;

import java.io.Serializable;

public class RecipePreview implements Serializable {
    public int recipeId;
    public String name;
    public String imgURL;

    public RecipePreview(int recipeId, String name, String imgURL) {
        this.name = name;
        this.imgURL = imgURL;
        this.recipeId = recipeId;
    }
}
