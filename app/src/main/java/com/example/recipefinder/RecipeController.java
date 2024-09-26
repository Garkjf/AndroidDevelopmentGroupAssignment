package com.example.recipefinder;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecipeController {
    private final Context context;
    private List<RecipePreview> recipes = new ArrayList<>();
    private Recipe recipe;

    public RecipeController(Context context) {
        this.context = context;
    }

    public interface ResponseListener<T> {
        void onError(String message);
        void onResponse(T response);
    }

    private void makeRequest(String url, Response.Listener<JSONObject> responseListener,
                             ResponseListener<?> listener) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                responseListener,
                error -> {
                    listener.onError("API Error");
                    Toast.makeText(context, "API Error", Toast.LENGTH_SHORT).show();
                }
        );

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public void getRecipes(@NonNull String query, ResponseListener<List<RecipePreview>> listener) {
        String url = String.format("https://www.themealdb.com/api/json/v1/1/search.php?s=%s",
                query.replace(" ", "%20"));

        makeRequest(url, response -> {
            try {
                recipes = JSONConverter.generateRecipes(response);
                listener.onResponse(recipes);
            } catch (JSONException e) {
                listener.onError("Cannot fetch recipes");
            }
        }, listener);
    }

    public void getFullRecipe(@NonNull String id, ResponseListener<Recipe> listener) {
        String url = String.format("https://www.themealdb.com/api/json/v1/1/lookup.php?i=%s", id);

        makeRequest(url, response -> {
            try {
                recipe = JSONConverter.generateFullRecipe(response);
                listener.onResponse(recipe);
            } catch (JSONException e) {
                listener.onError("Cannot fetch full recipe");
            }
        }, listener);
    }
}
