package com.example.recipefinder.model;

import androidx.annotation.NonNull;

public class Ingredient {
    private final String name, measure;

    public Ingredient(String name, String measure) {
        this.name = name;
        this.measure = measure;
    }

    public String getName() {
        return name;
    }

    public String getMeasure() {
        return measure;
    }

    @NonNull
    @Override
    public String toString() {
        return measure+" "+name;
    }
}