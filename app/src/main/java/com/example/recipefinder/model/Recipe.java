package com.example.recipefinder.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the full recipe
 */
public class Recipe {
    private final String instruction, description;
    private final List<Ingredient> ingredients = new ArrayList<>();

    /**
     * Constructor for Recipe
     * @param description Recipe description
     * @param instruction Recipe instruction
     */
    public Recipe(String description, String instruction) {
        this.description = description;
        this.instruction = instruction;
    }

    /**
     * Returns the instruction
     * @return Instruction
     */
    public String getInstruction() {
        return instruction;
    }

    /**
     * Returns the description
     * @return Description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the ingredients
     * @return Ingredients
     */
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
