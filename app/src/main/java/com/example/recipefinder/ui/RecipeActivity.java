package com.example.recipefinder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipefinder.R;
import com.example.recipefinder.RecipeController;
import com.example.recipefinder.adapter.IngredientAdapter;
import com.example.recipefinder.model.Recipe;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;

public class RecipeActivity extends AppCompatActivity {
    private RecyclerView ingredientList;
    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe);

        Intent i = getIntent();
        String id = i.getStringExtra("RECIPE_ID");
        String name = i.getStringExtra("RECIPE_NAME");
        String img = i.getStringExtra("RECIPE_IMG");
        String desc = i.getStringExtra("RECIPE_DESC");

        TextView recipeName = findViewById(R.id.ingr_name);
        recipeName.setText(name);

        TextView recipeDesc = findViewById(R.id.recipeDesc);
        recipeDesc.setText(desc);

        ImageView recipeImg = findViewById(R.id.ingr_img);

        Picasso.get().load(img).resize(300,300)
                .centerCrop().into(recipeImg);

        RecipeController controller = new RecipeController(RecipeActivity.this);

        ingredientList = findViewById(R.id.ingredientList);
        ingredientList.setLayoutManager(new LinearLayoutManager(RecipeActivity.this));
        ingredientList.setNestedScrollingEnabled(false);
        ingredientList.setVisibility(View.GONE);

        shimmerFrameLayout = findViewById(R.id.shimmerLayout);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();

        assert id != null;
        controller.getFullRecipe(id, new RecipeController.ResponseListener<Recipe>() {
            @Override
            public void onError(String message) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                ingredientList.setVisibility(View.VISIBLE);

                Toast.makeText(RecipeActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Recipe recipe) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);

                IngredientAdapter adapter = new IngredientAdapter(recipe.getIngredients(),
                        recipe.getMeasures());
                ingredientList.setAdapter(adapter);
                ingredientList.setVisibility(View.VISIBLE);

                TextView instructionText = findViewById(R.id.instructionText);
                instructionText.setText(recipe.getInstruction());
            }
        });
    }
}