package com.example.recipefinder.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipefinder.R;
import com.example.recipefinder.api.RecipeDAO;
import com.example.recipefinder.api.ResponseListener;
import com.example.recipefinder.model.RecipePreview;
import com.example.recipefinder.utils.SearchType;
import com.google.android.flexbox.FlexboxLayout;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {
    RecipeDAO recipeDAO;

    ImageView recipeImg;
    TextView recipeNameText;
    Button btnViewRecipe;

    RecipePreview randomRecipe = new RecipePreview("", "", "");

    private View view;
    private FlexboxLayout areaLayout;
    private FlexboxLayout categoryLayout;
    private CardView recipeCard;

    public HomeFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        recipeImg = view.findViewById(R.id.recipe_img);
        recipeNameText = view.findViewById(R.id.recipe_name);
        btnViewRecipe = view.findViewById(R.id.btn_viewRecipe);
        recipeCard = view.findViewById(R.id.previewCard);
        categoryLayout = view.findViewById(R.id.categoryLayout);
        areaLayout = view.findViewById(R.id.areaLayout);

        restoreSavedInstanceState(savedInstanceState);
        setupSearchBar();

        recipeDAO = RecipeDAO.getInstance(getActivity());

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

    private void restoreSavedInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            String recipeID = savedInstanceState.getString("RECIPE_ID", "");
            if (!recipeID.isEmpty()) {
                randomRecipe.setId(recipeID);
                randomRecipe.setName(savedInstanceState.getString("RECIPE_NAME", ""));
                randomRecipe.setImgURL(savedInstanceState.getString("IMG_URL", ""));
            }
        }
    }

    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("RECIPE_ID", randomRecipe.getId());
        savedInstanceState.putString("RECIPE_NAME", randomRecipe.getName());
        savedInstanceState.putString("IMG_URL", randomRecipe.getImgURL());
    }

    private void showErrorMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void navigateToSearchResult(SearchType type, String searchQuery) {
        Intent i = new Intent(getActivity(), SearchResultActivity.class);
        i.putExtra("SEARCH_TYPE", type.toString());
        i.putExtra("SEARCH_TERM", searchQuery);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
        recipeCard.setVisibility(View.INVISIBLE);

        if (!randomRecipe.getId().isEmpty()) {
            displayRandomRecipe();
            return;
        }

        recipeDAO.getRandomRecipe(new ResponseListener<RecipePreview>() {
            @Override
            public void onError(String message) {
                showErrorMessage(message);
            }
            @Override
            public void onResponse(RecipePreview response) {
                randomRecipe = response;
                displayRandomRecipe();
            }
        });
    }

    private void displayRandomRecipe() {
        Picasso.get().load(randomRecipe.getImgURL()).resize(300,300).
                centerCrop().into(recipeImg);
        recipeNameText.setText(randomRecipe.getName());
        btnViewRecipe.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RecipeActivity.class);
            intent.putExtra("RECIPE_ID", randomRecipe.getId());
            intent.putExtra("RECIPE_NAME", randomRecipe.getName());
            intent.putExtra("RECIPE_IMG", randomRecipe.getImgURL());
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
        recipeCard.setVisibility(View.VISIBLE);
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