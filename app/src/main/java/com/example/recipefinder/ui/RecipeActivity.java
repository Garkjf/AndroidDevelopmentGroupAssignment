package com.example.recipefinder.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.example.recipefinder.api.RecipeDAO;
import com.example.recipefinder.adapter.IngredientAdapter;
import com.example.recipefinder.api.ResponseListener;
import com.example.recipefinder.model.Recipe;
import com.example.recipefinder.model.RecipePreview;
import com.squareup.picasso.Picasso;

public class RecipeActivity extends AppCompatActivity {
    private RecyclerView ingredientList;
    RecipePreview recipe;
    RecipeDAO recipeDAO;

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
            Toast.makeText(this, "Invalid recipe", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recipe = (RecipePreview) i.getSerializableExtra("RECIPE");

        // Set the name and image of the recipe
        TextView recipeName = findViewById(R.id.ingr_name);
        recipeName.setText(recipe.getName());

        ImageView recipeImg = findViewById(R.id.ingr_img);
        Picasso.get().load(recipe.getImgURL())
                .resize(300,300).centerCrop().into(recipeImg);

        // Set up the ingredient list
        ingredientList = findViewById(R.id.ingredientList);
        ingredientList.setLayoutManager(new LinearLayoutManager(RecipeActivity.this));
        ingredientList.setNestedScrollingEnabled(false);
        ingredientList.setVisibility(View.INVISIBLE);

        getRecipe();
    }

    public static Intent newIntent(Context packageContext, RecipePreview preview){
        Intent i = new Intent(packageContext, RecipeActivity.class);
        i.putExtra("RECIPE", preview);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return i;
    }

    // Get the recipe
    private void getRecipe() {
        recipeDAO = RecipeDAO.getInstance(this);

        recipeDAO.getFullRecipe(recipe.getId(), new ResponseListener<Recipe>() {
            @Override
            public void onError(String message) {
                Toast.makeText(RecipeActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Recipe recipe) {
                // Set the recipe description and instructions
                TextView recipeDesc = findViewById(R.id.recipeDesc);
                recipeDesc.setText(recipe.getDescription());

                TextView instructionText = findViewById(R.id.instructionText);
                instructionText.setText(recipe.getInstruction());

                // Set the ingredients
                IngredientAdapter adapter = new IngredientAdapter(recipe.getIngredients());
                ingredientList.setAdapter(adapter);
                ingredientList.setVisibility(View.VISIBLE);
            }
        });
    }
}