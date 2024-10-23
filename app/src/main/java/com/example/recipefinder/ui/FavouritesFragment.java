package com.example.recipefinder.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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
    private FragmentActivity context;
    private RecipeDatabase recipeDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = requireActivity();
        recipeDB = new RecipeDatabase(context);

        List<RecipePreview> recipes = recipeDB.getRecipePreviews();

        adapter = new RecipeAdapter(recipes);
        adapter.setOnClickListener(item -> {
            Intent intent = RecipeActivity.newDBIntent(context, item);
            startActivity(intent);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);

        RecyclerView favouritesView = view.findViewById(R.id.favouritesView);
        favouritesView.setLayoutManager(new LinearLayoutManager(context));
        favouritesView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh data when the fragment is visible
        if (adapter != null) {
            List<RecipePreview> recipes = recipeDB.getRecipePreviews();
            adapter.updateRecipes(recipes);
        }
    }
}