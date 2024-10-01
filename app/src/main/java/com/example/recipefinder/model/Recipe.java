package com.example.recipefinder.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the full recipe
 */
public class Recipe {
    private final String instruction, description;
    private final List<String> ingredients = new ArrayList<>();
    private final List<String> measures = new ArrayList<>();

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
    public List<String> getIngredients() {
        return ingredients;
    }

    /**
     * Returns the measures
     * @return Measures
     */
    public List<String> getMeasures() {
        return measures;
    }

    /**
     * Adds an ingredient to the recipe
     * @param ingredient Ingredient name
     * @param measure Measure of the ingredient
     */
    public void addIngredient(String ingredient, String measure) {
        this.ingredients.add(ingredient);
        this.measures.add(measure);
    }
}
