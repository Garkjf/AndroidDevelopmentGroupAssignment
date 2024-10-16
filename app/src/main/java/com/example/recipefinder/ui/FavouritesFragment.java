package com.example.recipefinder.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recipefinder.R;
import com.example.recipefinder.adapter.RecipeAdapter;
import com.example.recipefinder.db.RecipeDatabase;
import com.example.recipefinder.model.RecipePreview;

import java.util.List;

public class FavouritesFragment extends Fragment {
    private RecipeAdapter adapter;

    public FavouritesFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);

        RecyclerView favouritesView = view.findViewById(R.id.favouritesView);
        favouritesView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<RecipePreview> recipes;
        try (RecipeDatabase recipeDB = new RecipeDatabase(getActivity())) {
            recipes = recipeDB.getRecipePreviews();
        }

        // Initialize the adapter with the fetched recipes
        RecipeAdapter adapter = new RecipeAdapter(recipes);
        favouritesView.setAdapter(adapter);
        adapter.setOnClickListener(item -> {
            Intent intent = RecipeActivity.newIntent(getActivity(), item, true);
            startActivity(intent);
        });
        this.adapter = adapter;

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh data when the fragment is visible
        if (adapter != null) {
            try (RecipeDatabase recipeDB = new RecipeDatabase(getActivity())) {
                List<RecipePreview> recipes = recipeDB.getRecipePreviews();
                adapter.updateRecipes(recipes);
            }
        }
    }
}