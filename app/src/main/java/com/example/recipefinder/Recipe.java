package com.example.recipefinder;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    String instruction;
    List<String> ingredients = new ArrayList<>();
    List<String> measures = new ArrayList<>();

    public Recipe(String instruction) {
        this.instruction = instruction;
    }
}
