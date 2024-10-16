package com.example.recipefinder.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the full recipe
 */
public class Recipe {
    public int recipeID;
    public String recipeName, imgURL, instruction, description;
    public List<Ingredient> ingredients = new ArrayList<>();

    /**
     * Constructor for Recipe
     * @param preview Recipe preview
     * @param description Recipe description
     * @param instruction Recipe instruction
     */
    public Recipe(RecipePreview preview, String description, String instruction) {
        this.recipeID = preview.recipeId;
        this.recipeName = preview.name;
        this.imgURL = preview.imgURL;
        this.description = description;
        this.instruction = instruction;
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
