package com.example.recipefinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONConverter {
    public static List<RecipePreview> generateRecipes(JSONObject response) throws JSONException {
        JSONArray recipesArray = response.getJSONArray("meals");
        List<RecipePreview> recipes = new ArrayList<>();

        for (int i = 0; i < recipesArray.length(); i++) {
            JSONObject recipeObject = recipesArray.getJSONObject(i);
            String desc = String.format("%s, %s", recipeObject.getString("strCategory"),
                    recipeObject.getString("strArea"));

            recipes.add(new RecipePreview(recipeObject.getString("idMeal"),
                    recipeObject.getString("strMeal"),
                    recipeObject.getString("strMealThumb"), desc)
            );
        }

        return recipes;
    }

    public static Recipe generateFullRecipe(JSONObject response) throws JSONException {
        JSONObject recipeItem = response.getJSONArray("meals").getJSONObject(0);
        String instructions = recipeItem.getString("strInstructions");

        Recipe recipe = new Recipe(instructions);

        for (int i = 1; i <= 20; i++) {
            String ingredient = recipeItem.getString("strIngredient"+i).trim();
            String measure = recipeItem.getString("strMeasure"+i).trim();

            if (measure.isEmpty() || ingredient.isEmpty()) break;

            recipe.addIngredient(ingredient, measure);
        }

        return recipe;
    }
}
