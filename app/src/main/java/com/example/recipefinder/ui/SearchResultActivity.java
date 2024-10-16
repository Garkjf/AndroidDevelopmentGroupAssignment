package com.example.recipefinder.ui;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.example.recipefinder.api.RecipeRepository;
import com.example.recipefinder.api.ResponseListener;
import com.example.recipefinder.model.RecipePreview;
import com.example.recipefinder.utils.SearchType;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

public class SearchResultActivity extends AppCompatActivity {
    private RecyclerView recipeView;
    private TextView countText, searchDesc;
    private RecipeRepository recipeRepo;
    private ShimmerFrameLayout shimmerLayout;

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

        recipeRepo = new RecipeRepository(SearchResultActivity.this);
        shimmerLayout = findViewById(R.id.shimmerLayout);

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

        searchDesc = findViewById(R.id.searchDesc);
        handleSearch(type, searchTerm);
    }

    private void handleSearch(SearchType type, String searchTerm) {
        RecipeSearchHandler searchHandler = null;

        switch (type) {
            case QUERY:
                searchDesc.setText(String.format("Results for: \"%s\"", searchTerm));
                searchHandler = (term, listener) -> recipeRepo.getRecipesByQuery(term, listener);
                break;
            case CATEGORY:
                searchDesc.setText(String.format("Category: %s", searchTerm));
                searchHandler = (term, listener) -> recipeRepo.getRecipesByCategory(term, listener);
                break;
            case AREA:
                searchDesc.setText(String.format("Area: %s", searchTerm));
                searchHandler = (term, listener) -> recipeRepo.getRecipesByArea(term, listener);
                break;
            default:
                Toast.makeText(this, "Invalid search type", Toast.LENGTH_SHORT).show();
                finish();
        }

        searchRecipes(searchTerm, searchHandler);
    }

    public static Intent setIntent(Context activity, SearchType searchType, String searchTerm) {
        Intent i = new Intent(activity, SearchResultActivity.class);
        i.putExtra("SEARCH_TYPE", searchType.toString());
        i.putExtra("SEARCH_TERM", searchTerm);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
    }

    // Interface to handle different search types
    interface RecipeSearchHandler {
        void search(String searchTerm, ResponseListener<List<RecipePreview>> listener);
    }

    // General method to handle recipe search
    private void searchRecipes(String searchTerm, RecipeSearchHandler searchHandler) {
        recipeView.setVisibility(View.GONE);
        shimmerLayout.setVisibility(View.VISIBLE);
        shimmerLayout.startShimmer();

        // Call the appropriate search method through the handler
        searchHandler.search(searchTerm, new ResponseListener<List<RecipePreview>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onError(String message) {
                shimmerLayout.stopShimmer();
                Toast.makeText(SearchResultActivity.this, message,
                        Toast.LENGTH_SHORT).show();
                countText.setText("No results found");
            }

            @Override
            public void onResponse(List<RecipePreview> recipes) {
                shimmerLayout.stopShimmer();
                shimmerLayout.setVisibility(View.GONE);
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
            Intent intent = RecipeActivity.newIntent(SearchResultActivity.this, item, false);
            startActivity(intent);
        });
        recipeView.setAdapter(adapter);
    }
}