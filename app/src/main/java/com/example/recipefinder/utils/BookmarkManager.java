package com.example.recipefinder.utils;

import android.content.Context;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.recipefinder.R;
import com.example.recipefinder.api.RecipeRepository;
import com.example.recipefinder.api.ResponseListener;
import com.example.recipefinder.db.RecipeDatabase;
import com.example.recipefinder.model.Recipe;
import com.example.recipefinder.model.RecipePreview;

import java.util.HashSet;

public class BookmarkManager {
    private final RecipeDatabase dbHelper;
    private final Context context;

    // Constructor
    public BookmarkManager(Context context) {
        dbHelper = new RecipeDatabase(context);
        this.context = context;
    }

    // Initializes the favorite button based on the recipe's favorite status.
    public void initializeFavoriteButton(ImageButton favButton, RecipePreview recipe) {
        HashSet<Integer> favourites = dbHelper.getAllRecipesID();

        // Set the button's background based on the current favorite status
        favButton.setBackgroundResource(favourites.contains(recipe.getRecipeId())
                ? R.drawable.baseline_bookmark_24
                : R.drawable.baseline_bookmark_border_24);

        // Set up click listener for the favorite button
        favButton.setOnClickListener(v -> toggleFavorite(favButton, recipe));
    }

    // Toggles the favorite status of the recipe.
    private void toggleFavorite(ImageButton favButton, RecipePreview recipe) {
        HashSet<Integer> favourites = dbHelper.getAllRecipesID();
        int recipeID = recipe.getRecipeId();

        if (favourites.contains(recipeID)) {
            // Remove from favorites
            dbHelper.deleteRecipe(recipeID);
            Toast.makeText(context, "Recipe removed from favourites", Toast.LENGTH_SHORT).show();
            favButton.setBackgroundResource(R.drawable.baseline_bookmark_border_24);
        } else {
            // Add to favorites
            fetchRecipeByID(recipeID);
            favButton.setBackgroundResource(R.drawable.baseline_bookmark_24);
        }
    }

    // Fetches the full recipe from the API and adds it to the database.
    private void fetchRecipeByID(int recipeId) {
        RecipeRepository recipeDAO = new RecipeRepository(context);

        recipeDAO.getFullRecipe(recipeId, new ResponseListener<Recipe>() {
            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Recipe recipe) {
               dbHelper.addRecipe(recipe);
               Toast.makeText(context, "Recipe added to favourites", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

