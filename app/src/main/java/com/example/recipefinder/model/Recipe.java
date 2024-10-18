package com.example.recipefinder.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the full recipe
 */
public class Recipe {
    private final int recipeID;
    private final String recipeName, imgURL, instruction, description;
    private final List<Ingredient> ingredients = new ArrayList<>();

    /**
     * Constructor for Recipe
     * @param preview Recipe preview
     * @param description Recipe description
     * @param instruction Recipe instruction
     */
    public Recipe(RecipePreview preview, String description, String instruction) {
        this.recipeID = preview.getRecipeId();
        this.recipeName = preview.getName();
        this.imgURL = preview.getImgURL();
        this.description = description;
        this.instruction = instruction;
    }


    public int getRecipeID() {
        return recipeID;
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

    /**
     * Adds an ingredient to the recipe
     * @param ingredient Ingredient name
     * @param measure Measure of the ingredient
     */
    public void addIngredient(String ingredient, String measure) {
        this.ingredients.add(new Ingredient(ingredient, measure));
    }
}
