package com.example.recipefinder.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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
import com.example.recipefinder.db.RecipeDAO;
import com.example.recipefinder.model.Ingredient;
import com.example.recipefinder.model.RecipePreview;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ShoppingListFragment extends Fragment {
    private List<RecipePreview> recipes;
    private RecipeDAO recipeDAO;
    private IngredientAdapter ingredientAdapter;
    private FragmentActivity context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = requireActivity();
        recipeDAO = new RecipeDAO(context);
        recipes = recipeDAO.getRecipePreviews();
        if (recipes == null) {
            recipes = new ArrayList<>();
        }

        ingredientAdapter = new IngredientAdapter(new ArrayList<>());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        RecyclerView shoppingListView = view.findViewById(R.id.shoppingListView);
        shoppingListView.setLayoutManager(new LinearLayoutManager(context));
        shoppingListView.setAdapter(ingredientAdapter);

        Spinner recipeSpinner = view.findViewById(R.id.recipeSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,
                recipes.stream().map(RecipePreview::getName).collect(Collectors.toList()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recipeSpinner.setAdapter(adapter);

        recipeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {
                RecipePreview selectedRecipe = recipes.get(position);
                List<Ingredient> ingredients = recipeDAO.getIngredients(selectedRecipe.getApiId());
                if (ingredients != null) {
                    ingredientAdapter.setIngredients(ingredients);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        return view;
    }
}