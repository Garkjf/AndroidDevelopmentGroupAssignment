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

/**
 * DAO (Data Access Object) for retrieving recipes
 */
public class RecipeDAO {
    private static RecipeDAO instance;
    private final Context context;
    private final String urlPrefix = "https://www.themealdb.com/api/json/v1/1/";

    /**
     * Constructor for RecipeDAO
     * @param context App context
     */
    public RecipeDAO(Context context) {
        this.context = context.getApplicationContext();
    }

    public static RecipeDAO getInstance(Context context) {
        if (instance == null) {
            instance = new RecipeDAO(context);
        }
        return instance;
    }

    /**
     * Makes a request to the API
     * @param url URL to request
     * @param responseListener Listener for response
     * @param listener Listener for errors
     */
    private void makeRequest(String url, Response.Listener<JSONObject> responseListener,
                             ResponseListener<?> listener) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                responseListener, error -> handleVolleyError(error, listener)
        );

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     * Handles Volley errors
     * @param error Volley error
     * @param listener Listener for errors
     */
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

    /**
     * General method to retrieve a list of recipes
     * @param url URL to request
     * @param listener Listener for response
     */
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

    /**
     * Retrieves recipes by query
     * @param query Search query
     * @param listener Listener for response
     */
    public void getRecipesByQuery(@NonNull String query, ResponseListener<List<RecipePreview>> listener) {
        String formattedQuery = query.replace(" ", "%20");
        String url = String.format(urlPrefix + "search.php?s=%s", formattedQuery);
        fetchRecipes(url, listener);
    }

    /**
     * Retrieves recipes by category
     * @param category Category to search for
     * @param listener Listener for response
     */
    public void getRecipesByCategory(@NonNull String category, ResponseListener<List<RecipePreview>> listener) {
        String url = String.format(urlPrefix + "filter.php?c=%s", category);
        fetchRecipes(url, listener);
    }

    /**
     * Retrieves recipes by area
     * @param area Area to search for
     * @param listener Listener for response
     */
    public void getRecipesByArea(@NonNull String area, ResponseListener<List<RecipePreview>> listener) {
        String url = String.format(urlPrefix+"filter.php?a=%s", area);
        fetchRecipes(url, listener);
    }

    /**
     * Retrieves a list of categories
     * @param listener Listener for response
     */
    public void getCategories(ResponseListener<List<String>> listener) {
        String url = urlPrefix+"list.php?c=list";

        makeRequest(url, response -> {
            try {
                List<String> categories = JSONConverter.getCategories(response);
                listener.onResponse(categories);
            } catch (JSONException e) {
                listener.onError("Cannot fetch categories");
            }
        }, listener);
    }

    /**
     * Retrieves a list of areas
     * @param listener Listener for response
     */
    public void getAreas(ResponseListener<List<String>> listener) {
        String url = urlPrefix+"list.php?a=list";

        makeRequest(url, response -> {
            try {
                List<String> areas = JSONConverter.getAreas(response);
                listener.onResponse(areas);
            } catch (JSONException e) {
                listener.onError("Cannot fetch areas");
            }
        }, listener);
    }

    /**
     * Retrieves a full recipe
     * @param id Recipe ID
     * @param listener Listener for response
     */
    public void getFullRecipe(@NonNull String id, ResponseListener<Recipe> listener) {
        String url = String.format(urlPrefix+"lookup.php?i=%s", id);

        makeRequest(url, response -> {
            try {
                Recipe recipe = JSONConverter.getFullRecipe(response);
                listener.onResponse(recipe);
            } catch (JSONException e) {
                listener.onError("Cannot fetch full recipe");
            }
        }, listener);
    }
}
