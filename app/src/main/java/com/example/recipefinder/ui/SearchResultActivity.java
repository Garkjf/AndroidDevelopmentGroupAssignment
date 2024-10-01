package com.example.recipefinder.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.example.recipefinder.adapter.RecipeAdapter;
import com.example.recipefinder.api.RecipeDAO;
import com.example.recipefinder.api.ResponseListener;
import com.example.recipefinder.model.RecipePreview;
import com.example.recipefinder.utils.SearchType;

import java.util.List;

public class SearchResultActivity extends AppCompatActivity {
    private RecyclerView recipeView;
    private TextView countText;
    private RecipeDAO recipeDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recipeView = findViewById(R.id.recipeView);
        recipeView.setHasFixedSize(true);
        recipeView.setLayoutManager(new LinearLayoutManager(this));

        countText = findViewById(R.id.countText);

        Intent i = getIntent();
        String searchType = i.getStringExtra("SEARCH_TYPE");
        SearchType type = SearchType.valueOf(searchType);
        String searchTerm = i.getStringExtra("SEARCH_TERM");

        if (searchType == null || searchTerm == null) {
            Toast.makeText(this, "Invalid search parameters", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recipeDAO = RecipeDAO.getInstance(this);

        TextView searchDesc = findViewById(R.id.searchDesc);
        RecipeSearchHandler searchHandler = null;

        switch (type) {
            case QUERY:
                searchDesc.setText(String.format("Results for: \"%s\"", searchTerm));
                searchHandler = (term, listener) -> recipeDAO.getRecipesByQuery(term, listener);
                break;
            case CATEGORY:
                searchDesc.setText(String.format("Category: %s", searchTerm));
                searchHandler = (term, listener) -> recipeDAO.getRecipesByCategory(term, listener);
                break;
            case AREA:
                searchDesc.setText(String.format("Area: %s", searchTerm));
                searchHandler = (term, listener) -> recipeDAO.getRecipesByArea(term, listener);
                break;
            default:
                Toast.makeText(this, "Invalid search type", Toast.LENGTH_SHORT).show();
                finish();
        }

        assert searchHandler != null;
        searchRecipes(searchTerm, searchHandler);
    }

    // Interface to handle different search types
    interface RecipeSearchHandler {
        void search(String searchTerm, ResponseListener<List<RecipePreview>> listener);
    }

    // General method to handle recipe search
    private void searchRecipes(String searchTerm, RecipeSearchHandler searchHandler) {

        // Call the appropriate search method through the handler
        searchHandler.search(searchTerm, new ResponseListener<List<RecipePreview>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onError(String message) {
                Toast.makeText(SearchResultActivity.this, message,
                        Toast.LENGTH_SHORT).show();
                countText.setText("No results found");
            }

            @Override
            public void onResponse(List<RecipePreview> recipes) {
                recipeView.setVisibility(View.VISIBLE);

                loadRecipes(recipes);
            }
        });
    }

    // Load recipes into the RecyclerView
    @SuppressLint("DefaultLocale")
    public void loadRecipes(List<RecipePreview> recipes) {
        RecipeAdapter adapter = new RecipeAdapter(recipes);
        countText.setText(String.format("%d results found", recipes.size()));

        adapter.setOnClickListener(item -> {
            Intent intent = new Intent(SearchResultActivity.this, RecipeActivity.class);

            intent.putExtra("RECIPE_ID", item.getId());
            intent.putExtra("RECIPE_NAME", item.getName());
            intent.putExtra("RECIPE_IMG", item.getImgURL());
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

            startActivity(intent);
        });
        recipeView.setAdapter(adapter);
    }
}