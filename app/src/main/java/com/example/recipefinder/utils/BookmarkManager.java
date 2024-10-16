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

    public BookmarkManager(Context context) {
        dbHelper = new RecipeDatabase(context);
        this.context = context;
    }

    /**
     * Initializes the favorite button based on the recipe's favorite status.
     *
     * @param favButton   The button to toggle favorite status.
     * @param recipe      The recipe to check.
     */
    public void initializeFavoriteButton(ImageButton favButton, RecipePreview recipe) {
        HashSet<Integer> favourites = dbHelper.getAllRecipesID();

        // Set the button's background based on the current favorite status
        if (favourites.contains(recipe.recipeId)) {
            favButton.setBackgroundResource(R.drawable.baseline_bookmark_24); // Filled bookmark
        } else {
            favButton.setBackgroundResource(R.drawable.baseline_bookmark_border_24); // Outline bookmark
        }

        // Set up click listener for the favorite button
        favButton.setOnClickListener(v -> toggleFavorite(favButton, recipe));
    }

    /**
     * Toggles the favorite status of the recipe.
     *
     * @param favButton   The button to toggle favorite status.
     * @param recipe      The recipe to toggle.
     */
    private void toggleFavorite(ImageButton favButton, RecipePreview recipe) {
        HashSet<Integer> favourites = dbHelper.getAllRecipesID();

        if (favourites.contains(recipe.recipeId)) {
            // Remove from favorites
            dbHelper.deleteRecipe(recipe.recipeId);
            Toast.makeText(context, "Recipe removed from favourites", Toast.LENGTH_SHORT).show();
            favButton.setBackgroundResource(R.drawable.baseline_bookmark_border_24);
        } else {
            // Add to favorites
            fetchRecipeByID(recipe.recipeId);
            favButton.setBackgroundResource(R.drawable.baseline_bookmark_24);
        }
    }

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

