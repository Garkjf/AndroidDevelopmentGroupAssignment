package com.example.recipefinder.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipefinder.R;
import com.example.recipefinder.api.RecipeRepository;
import com.example.recipefinder.adapter.IngredientAdapter;
import com.example.recipefinder.api.ResponseListener;
import com.example.recipefinder.db.RecipeDatabase;
import com.example.recipefinder.model.Recipe;
import com.example.recipefinder.model.RecipePreview;
import com.example.recipefinder.utils.BookmarkManager;
import com.squareup.picasso.Picasso;

public class RecipeActivity extends AppCompatActivity {
    private RecyclerView ingredientList;
    private RecipePreview preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent i = getIntent();
        if (!i.hasExtra("RECIPE")) {
            Toast.makeText(this, "Invalid preview", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        preview = (RecipePreview) i.getSerializableExtra("RECIPE");

        // Set the favourite button
        ImageButton favButton = findViewById(R.id.favourite_button);
        BookmarkManager bookmarkManager = new BookmarkManager(RecipeActivity.this);
        bookmarkManager.initializeFavoriteButton(favButton, preview);

        // Set up the ingredient list
        ingredientList = findViewById(R.id.ingredientList);
        ingredientList.setLayoutManager(new LinearLayoutManager(RecipeActivity.this));
        ingredientList.setNestedScrollingEnabled(false);
        ingredientList.setVisibility(View.INVISIBLE);

        TextView recipeName = findViewById(R.id.recipe_name);
        recipeName.setText(preview.getName());

        ImageView recipeImg = findViewById(R.id.recipe_image);
        Picasso.get().load(preview.getImgURL()).resize(300,300)
                .centerCrop().into(recipeImg);

        boolean isLoaded = i.getBooleanExtra("LOADED", false);
        if (isLoaded) getRecipeFromDatabase();
        else getRecipeFromAPI();
    }

    // Create an intent to start the activity
    public static Intent newDBIntent(Context packageContext, RecipePreview preview) {
        Intent i = new Intent(packageContext, RecipeActivity.class);
        i.putExtra("RECIPE", preview);
        i.putExtra("LOADED", true);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return i;
    }

    public static Intent newAPIIntent(Context packageContext, RecipePreview preview) {
        Intent i = new Intent(packageContext, RecipeActivity.class);
        i.putExtra("RECIPE", preview);
        i.putExtra("LOADED", false);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return i;
    }

    // Get the recipe from the API
    private void getRecipeFromAPI() {
        RecipeRepository recipeDAO = new RecipeRepository(RecipeActivity.this);

        // Make the asynchronous API call
        recipeDAO.getFullRecipe(preview.getRecipeId(), new ResponseListener<Recipe>() {
            @Override
            public void onError(String message) {
                Toast.makeText(RecipeActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Recipe recipe) {
                setupRecipe(recipe);
            }
        });
    }

    // Get the recipe from the database
    private void getRecipeFromDatabase() {
        Recipe recipe;
        try (RecipeDatabase recipeDB = new RecipeDatabase(RecipeActivity.this)) {
            recipe = recipeDB.getRecipe(preview.getRecipeId());
        }

        if (recipe == null) {
            Toast.makeText(this, "Recipe not found.", Toast.LENGTH_SHORT).show();
        } else {
            setupRecipe(recipe);
        }
    }

    // Setup the recipe
    private void setupRecipe(Recipe recipe) {
        TextView recipeDesc = findViewById(R.id.recipe_description);
        recipeDesc.setText(recipe.getDescription());

        TextView instructionText = findViewById(R.id.instruction_text);
        instructionText.setText(recipe.getInstruction());

        // Set the ingredients
        IngredientAdapter adapter = new IngredientAdapter(recipe.getIngredients());
        ingredientList.setAdapter(adapter);
        ingredientList.setVisibility(View.VISIBLE);
    }
}