package com.example.recipefinder.utils;

import com.example.recipefinder.model.Recipe;
import com.example.recipefinder.model.RecipePreview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONConverter {
    // Returns a list of RecipePreview objects
    public static List<RecipePreview> getRecipes(JSONObject response) throws JSONException {
        JSONArray recipesArray = response.getJSONArray("meals");
        List<RecipePreview> recipes = new ArrayList<>();

        for (int i = 0; i < recipesArray.length(); i++) {
            JSONObject recipeObject = recipesArray.getJSONObject(i);

            recipes.add(new RecipePreview(recipeObject.getInt("idMeal"),
                    recipeObject.getString("strMeal"),
                    recipeObject.getString("strMealThumb"))
            );
        }

        return recipes;
    }

    // Returns a Recipe object based on the response.
    public static Recipe getFullRecipe(JSONObject response) throws JSONException {
        JSONObject recipeItem = response.getJSONArray("meals").getJSONObject(0);
        String instructions = recipeItem.getString("strInstructions");
        String desc = String.format("%s, %s", recipeItem.getString("strCategory"),
                recipeItem.getString("strArea"));

        Recipe recipe = new Recipe(0, recipeItem.getInt("idMeal"),
                recipeItem.getString("strMeal"),
                recipeItem.getString("strMealThumb"), desc, instructions);

        for (int i = 1; i <= 20; i++) {
            String ingredient = recipeItem.getString("strIngredient"+i).trim();
            String measure = recipeItem.getString("strMeasure"+i).trim();

            if (measure.isEmpty() || ingredient.isEmpty()) break;

            recipe.addIngredient(ingredient, measure);
        }

        return recipe;
    }
}
