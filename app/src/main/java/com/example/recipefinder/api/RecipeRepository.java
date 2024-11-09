package com.example.recipefinder.api;

import android.content.Context;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.example.recipefinder.model.Recipe;
import com.example.recipefinder.model.RecipePreview;
import com.example.recipefinder.utils.JSONConverter;
import com.example.recipefinder.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class RecipeRepository {
    private final Context context;
    private final String urlPrefix = "https://www.themealdb.com/api/json/v1/1/";

    // Constructor
    public RecipeRepository(Context context) {
        this.context = context.getApplicationContext();
    }

    // Makes a request to the API
    private void makeRequest(String url, Response.Listener<JSONObject> responseListener,
                             ResponseListener<?> listener) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                responseListener, error -> handleVolleyError(error, listener)
        );

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    // Handles Volley errors
    private void handleVolleyError(VolleyError error, ResponseListener<?> listener) {
        String errorMessage = "API Error";
        if (error.networkResponse != null) {
            errorMessage = "Error " + error.networkResponse.statusCode + ": " +
                    new String(error.networkResponse.data);
        } else if (error.getMessage() != null) {
            errorMessage = error.getMessage();
        }
        listener.onError(errorMessage);
    }

    // General method to retrieve a list of recipes
    private void fetchRecipes(String url, ResponseListener<List<RecipePreview>> listener) {
        makeRequest(url, response -> {
            try {
                List<RecipePreview> recipes = JSONConverter.getRecipes(response);
                listener.onResponse(recipes);
            } catch (JSONException e) {
                listener.onError("Cannot fetch recipes");
            }
        }, listener);
    }

    // Retrieves recipes by search query
    public void getRecipesByQuery(@NonNull String query, ResponseListener<List<RecipePreview>> listener) {
        String formattedQuery = query.replace(" ", "%20");
        String url = String.format(urlPrefix + "search.php?s=%s", formattedQuery);
        fetchRecipes(url, listener);
    }

    // Retrieves recipes by category
    public void getRecipesByCategory(@NonNull String category, ResponseListener<List<RecipePreview>> listener) {
        String url = String.format(urlPrefix + "filter.php?c=%s", category);
        fetchRecipes(url, listener);
    }

    // Retrieves recipes by area
    public void getRecipesByArea(@NonNull String area, ResponseListener<List<RecipePreview>> listener) {
        String url = String.format(urlPrefix + "filter.php?a=%s", area);
        fetchRecipes(url, listener);
    }

    // Retrieves a full recipe
    public void getFullRecipe(int recipeID, ResponseListener<Recipe> listener) {
        String url = String.format(urlPrefix + "lookup.php?i=%s", recipeID);

        makeRequest(url, response -> {
            try {
                Recipe recipe = JSONConverter.getFullRecipe(response);
                listener.onResponse(recipe);
            } catch (JSONException e) {
                listener.onError("Cannot fetch full recipe");
            }
        }, listener);
    }

    // Retrieves a random recipe
    public void getRandomRecipe(ResponseListener<RecipePreview> listener) {
        String url = urlPrefix + "random.php";
        makeRequest(url, response -> {
            try {
                RecipePreview recipe = JSONConverter.getRecipes(response).get(0);
                listener.onResponse(recipe);
            } catch (JSONException e) {
                listener.onError("Recipes not found");
            }
        }, listener);
    }
}
