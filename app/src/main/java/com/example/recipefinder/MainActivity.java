package com.example.recipefinder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipefinder.adapter.RecipeAdapter;
import com.example.recipefinder.model.RecipePreview;
import com.example.recipefinder.ui.RecipeActivity;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recipeView;
    private RecipeController controller;
    private TextView countText;

    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        controller = new RecipeController(MainActivity.this);

        recipeView = findViewById(R.id.recipeView);
        recipeView.setHasFixedSize(true);
        recipeView.setLayoutManager(new LinearLayoutManager(this));

        countText = findViewById(R.id.countText);
        shimmerFrameLayout = findViewById(R.id.shimmerLayout);

        SearchView searchBar = findViewById(R.id.searchBar);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String searchTerm = query.toLowerCase();

                if (searchTerm.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter search term",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                searchRecipes(searchTerm);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void searchRecipes(String searchTerm) {
        recipeView.setVisibility(View.GONE);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();

        controller.getRecipes(searchTerm,
            new RecipeController.ResponseListener<List<RecipePreview>>() {
                @Override
                public void onError(String message) {
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    recipeView.setVisibility(View.VISIBLE);

                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(List<RecipePreview> recipes) {
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    recipeView.setVisibility(View.VISIBLE);

                    loadRecipes(recipes);
                }
        });
    }

    @SuppressLint("DefaultLocale")
    public void loadRecipes(List<RecipePreview> recipes) {
        RecipeAdapter adapter = new RecipeAdapter(recipes);
        countText.setText(String.format("%d results found", recipes.size()));

        adapter.setOnClickListener(item -> {
            Intent intent = new Intent(MainActivity.this, RecipeActivity.class);

            intent.putExtra("RECIPE_ID", item.getId());
            intent.putExtra("RECIPE_NAME", item.getName());
            intent.putExtra("RECIPE_IMG", item.getImgURL());
            intent.putExtra("RECIPE_DESC", item.getDesc());

            startActivity(intent);
        });
        recipeView.setAdapter(adapter);
    }
}