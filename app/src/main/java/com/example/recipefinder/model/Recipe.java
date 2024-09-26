package com.example.recipefinder.model;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private final String instruction;
    private final List<String> ingredients = new ArrayList<>();
    private final List<String> measures = new ArrayList<>();

    public Recipe(String instruction) {
        this.instruction = instruction;
    }

    public String getInstruction() {
        return instruction;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void addIngredient(String ingredient, String measure) {
        this.ingredients.add(ingredient);
        this.measures.add(measure);
    }

    public List<String> getMeasures() {
        return measures;
    }
}
