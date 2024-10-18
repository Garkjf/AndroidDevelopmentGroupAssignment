package com.example.recipefinder.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.recipefinder.R;
import com.example.recipefinder.adapter.RecipeAdapter;
import com.example.recipefinder.api.RecipeRepository;
import com.example.recipefinder.api.ResponseListener;
import com.example.recipefinder.model.RecipePreview;
import com.example.recipefinder.utils.SearchType;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecipeRepository recipeRepo;
    private RecyclerView recipeView;
    private RecipePreview randomRecipe = new RecipePreview(0, "", "");

    private View view;
    private FlexboxLayout areaLayout, categoryLayout;
    private ShimmerFrameLayout shimmerLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            randomRecipe = (RecipePreview) savedInstanceState.getSerializable("RECIPE");
        }
        recipeRepo = new RecipeRepository(requireActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        recipeView = view.findViewById(R.id.recipeView);
        recipeView.setHasFixedSize(true);
        recipeView.setLayoutManager(new LinearLayoutManager(getActivity()));

        categoryLayout = view.findViewById(R.id.categoryLayout);
        areaLayout = view.findViewById(R.id.areaLayout);
        shimmerLayout = view.findViewById(R.id.shimmerLayout);

        setupSearchBar();

        loadRandomRecipe();
        loadCategories();
        loadAreas();

        return view;
    }

    private void setupSearchBar() {
        SearchView searchBar = view.findViewById(R.id.searchBar);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String searchTerm = query.toLowerCase();

                if (searchTerm.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter search term",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }

                navigateToSearchResult(SearchType.QUERY, searchTerm);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("RECIPE", randomRecipe);
    }

    private void showErrorMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void navigateToSearchResult(SearchType type, String searchQuery) {
        Intent i = SearchResultActivity.setIntent(getActivity(), type, searchQuery);
        startActivity(i);
    }

    @NonNull
    private Button createButton(String text) {
        Button button = new Button(getActivity());
        button.setText(text);
        button.setAllCaps(false);
        button.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.btn_background));
        button.setTextColor(Color.WHITE);

        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(8, 8, 8, 8);
        button.setLayoutParams(params);

        return button;
    }

    // Random recipe functions
    private void loadRandomRecipe() {
        shimmerLayout.setVisibility(View.GONE);
        if (randomRecipe.getRecipeId() != 0) {
            displayRandomRecipe();
            return;
        }

        recipeView.setVisibility(View.INVISIBLE);
        recipeRepo.getRandomRecipe(new ResponseListener<RecipePreview>() {
            @Override
            public void onError(String message) {
                shimmerLayout.stopShimmer();
                showErrorMessage(message);
            }
            @Override
            public void onResponse(RecipePreview response) {
                shimmerLayout.stopShimmer();
                shimmerLayout.setVisibility(View.GONE);
                randomRecipe = response;
                displayRandomRecipe();
                recipeView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void displayRandomRecipe() {
        List<RecipePreview> recipes = new ArrayList<>();
        recipes.add(randomRecipe);

        RecipeAdapter adapter = new RecipeAdapter(recipes);
        adapter.setOnClickListener(item -> {
            Intent intent = RecipeActivity.newIntent(getActivity(), item, false);
            startActivity(intent);
        });
        recipeView.setAdapter(adapter);
    }

    private void loadCategories() {
        String[] categories = getResources().getStringArray(R.array.categories);

        for (String category : categories) {
            Button btn = createButton(category);
            categoryLayout.addView(btn);
            btn.setOnClickListener(v -> navigateToSearchResult(SearchType.CATEGORY, category));
        }
    }

    private void loadAreas() {
        String[] areas = getResources().getStringArray(R.array.areas);

        for (String area : areas) {
            Button btn = createButton(area);
            areaLayout.addView(btn);
            btn.setOnClickListener(v -> navigateToSearchResult(SearchType.AREA, area));
        }
    }
}