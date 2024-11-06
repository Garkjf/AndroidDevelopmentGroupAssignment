package com.example.recipefinder.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the full recipe
 */
public class Recipe {
    private final int recipeID, apiID;
    private final String recipeName, imgURL, instruction, description;
    private final List<Ingredient> ingredients = new ArrayList<>();

    // Constructor
    public Recipe(int recipeID, int apiID, String recipeName, String imgURL,
                  String description, String instruction) {
        this.recipeID = recipeID;
        this.apiID = apiID;
        this.recipeName = recipeName;
        this.imgURL = imgURL;
        this.description = description;
        this.instruction = instruction;
    }

    // Getters
    public int getRecipeID() {
        return recipeID;
    }

    public int getApiID() {
        return apiID;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public String getImgURL() {
        return imgURL;
    }

    public String getInstruction() {
        return instruction;
    }

    public String getDescription() {
        return description;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    // Adds an ingredient to the recipe
    public void addIngredient(String ingredient, String measure) {
        this.ingredients.add(new Ingredient(ingredient, measure));
    }
}
