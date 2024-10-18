package com.example.recipefinder.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.recipefinder.R;
import com.example.recipefinder.adapter.IngredientAdapter;
import com.example.recipefinder.db.RecipeDatabase;
import com.example.recipefinder.model.Ingredient;
import com.example.recipefinder.model.Recipe;
import com.example.recipefinder.model.RecipePreview;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ShoppingListFragment extends Fragment {
    private List<RecipePreview> recipes;
    private ArrayAdapter<String> adapter;
    private RecipeDatabase recipeDB;
    private IngredientAdapter ingredientAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeDB = new RecipeDatabase(getActivity());
        recipes = recipeDB.getRecipePreviews();

        adapter = new ArrayAdapter<>(requireActivity(),
                android.R.layout.simple_spinner_item,
                recipes.stream().map(RecipePreview::getName).collect(Collectors.toList())
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ingredientAdapter = new IngredientAdapter(new ArrayList<>());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        RecyclerView shoppingListView = view.findViewById(R.id.shoppingListView);
        shoppingListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        shoppingListView.setAdapter(ingredientAdapter);

        Spinner recipeSpinner = view.findViewById(R.id.recipeSpinner);
        recipeSpinner.setAdapter(adapter);

        recipeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {
                RecipePreview selectedRecipe = recipes.get(position);
                Recipe recipe = recipeDB.getRecipe(selectedRecipe.getRecipeId());
                List<Ingredient> ingredients = recipe.getIngredients();
                ingredientAdapter.setIngredients(ingredients);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        return view;
    }
}