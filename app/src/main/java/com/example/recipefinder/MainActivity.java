package com.example.recipefinder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.List;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.OnItemClickListener {
    private RecyclerView recipeView;
    private RecipeController controller;
    private TextView countText;

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

        SearchView searchBar = findViewById(R.id.searchBar);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String searchTerm = query.toLowerCase();

                controller.getRecipes(searchTerm,
                    new RecipeController.ResponseListener<List<RecipePreview>>() {
                        @Override
                        public void onError(String message) {
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        }

                        @SuppressLint("DefaultLocale")
                        @Override
                        public void onResponse(List<RecipePreview> recipes) {
                            RecipeAdapter adapter = new RecipeAdapter(recipes, MainActivity.this);
                            countText.setText(String.format("%d results found", recipes.size()));
                            recipeView.setAdapter(adapter);
                        }
                });

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onViewClick(RecipePreview item) {
        Intent intent = new Intent(MainActivity.this, RecipeActivity.class);

        intent.putExtra("RECIPE_ID", item.getId());
        intent.putExtra("RECIPE_NAME", item.getName());
        intent.putExtra("RECIPE_IMG", item.getImgURL());
        intent.putExtra("RECIPE_DESC", item.getDesc());

        startActivity(intent);
    }
}