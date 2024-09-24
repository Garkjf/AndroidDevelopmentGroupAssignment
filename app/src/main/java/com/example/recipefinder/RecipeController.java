package com.example.recipefinder;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecipeController {
    private final Context context;
    private final List<RecipePreview> recipes = new ArrayList<>();
    private Recipe recipe;

    public RecipeController(Context context) {
        this.context = context;
    }

    public interface SearchResponseListener {
        void onError(String message);
        void onResponse(List<RecipePreview> recipes);
    }

    public interface ViewResponseListener {
        void onError(String message);
        void onResponse(Recipe recipe);
    }

    public void getRecipes(@NonNull String query, SearchResponseListener listener) {
        String url = String.format("https://www.themealdb.com/api/json/v1/1/search.php?s=%s",
                query.replace(" ", "%20"));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        generateRecipes(response);
                        listener.onResponse(recipes);
                    } catch (JSONException e) {
                        listener.onError("Cannot fetch recipe");
                    }
                },
                error -> Toast.makeText(context,"API Error", Toast.LENGTH_SHORT).show()
        );

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public void getFullRecipe(@NonNull String id, ViewResponseListener listener) {
        String url = String.format("https://www.themealdb.com/api/json/v1/1/lookup.php?i=%s", id);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        generateFullRecipe(response);
                        listener.onResponse(recipe);
                    } catch (JSONException e) {
                        listener.onError("Cannot fetch recipe");
                    }
                },
                error -> Toast.makeText(context,"API Error", Toast.LENGTH_SHORT).show()
        );

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    private void generateRecipes(JSONObject response) throws JSONException {
        recipes.clear();
        JSONArray recipesArray = response.getJSONArray("meals");

        for (int i = 0; i < recipesArray.length(); i++) {
            JSONObject recipeObject = recipesArray.getJSONObject(i);
            String desc = String.format("%s, %s",
                    recipeObject.getString("strCategory"),
                    recipeObject.getString("strArea"));

            recipes.add(new RecipePreview(recipeObject.getString("idMeal"),
                    recipeObject.getString("strMeal"),
                    recipeObject.getString("strMealThumb"), desc)
            );
        }
    }

    private void generateFullRecipe(JSONObject response) throws JSONException {
        JSONObject recipeItem = response.getJSONArray("meals").getJSONObject(0);
        String instructions = recipeItem.getString("strInstructions");

        recipe = new Recipe(instructions);

        for (int i = 1; i <= 20; i++) {
            String ingredient = recipeItem.getString("strIngredient"+i).trim();
            String measure = recipeItem.getString("strMeasure"+i).trim();

            if (measure.isEmpty() || ingredient.isEmpty()) break;

            recipe.ingredients.add(ingredient);
            recipe.measures.add(measure);
        }
    }
}
