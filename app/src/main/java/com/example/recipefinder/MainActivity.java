package com.example.recipefinder;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.recipefinder.api.RecipeDAO;
import com.example.recipefinder.api.ResponseListener;
import com.example.recipefinder.model.RecipePreview;
import com.example.recipefinder.ui.RecipeActivity;
import com.example.recipefinder.ui.SearchResultActivity;
import com.example.recipefinder.utils.SearchType;
import com.google.android.flexbox.FlexboxLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecipeDAO recipeDAO;
    private ImageView recipeImg;
    private TextView recipeNameText;
    private Button btnViewRecipe;
    private RecipePreview randomRecipe = new RecipePreview("", "", "");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (savedInstanceState != null) {
            String recipeID = savedInstanceState.getString("RECIPE_ID", "");
            randomRecipe.setId(recipeID);
            String recipeName = savedInstanceState.getString("RECIPE_NAME", "");
            randomRecipe.setName(recipeName);
            String imgURL = savedInstanceState.getString("IMG_URL", "");
            randomRecipe.setImgURL(imgURL);
        }

        recipeDAO = RecipeDAO.getInstance(this);

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

                createSearchRequest(SearchType.QUERY, searchTerm);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        Toast.makeText(MainActivity.this, "Loading...", Toast.LENGTH_SHORT).show();

        getRandomRecipe();
        setupCategories();
        setupAreas();
    }

    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("RECIPE_ID", randomRecipe.getId());
        savedInstanceState.putString("RECIPE_NAME", randomRecipe.getName());
        savedInstanceState.putString("IMG_URL", randomRecipe.getImgURL());
    }

    private void createSearchRequest(@NonNull SearchType type, String searchTerm) {
        Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
        intent.putExtra("SEARCH_TYPE", type.name());
        intent.putExtra("SEARCH_TERM", searchTerm);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @NonNull
    private TextView getTextView(String text) {
        TextView textView = new TextView(MainActivity.this);
        textView.setText(text);
        textView.setPadding(16, 16, 16, 16);
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundResource(R.drawable.badge_background);

        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(8, 8, 8, 8);
        textView.setLayoutParams(params);

        textView.setClickable(true);
        textView.setFocusable(true);
        return textView;
    }

    private void getRandomRecipe() {
        CardView recipeCard = findViewById(R.id.previewCard);
        recipeCard.setVisibility(View.INVISIBLE);
        TextView randomRecipeText = findViewById(R.id.randomRecipeText);
        randomRecipeText.setVisibility(View.INVISIBLE);

        recipeImg = findViewById(R.id.recipe_img);
        recipeNameText = findViewById(R.id.recipe_name);
        btnViewRecipe = findViewById(R.id.btn_viewRecipe);

        if (!randomRecipe.getId().isEmpty()) {
            setupRecipe(randomRecipe);
            recipeCard.setVisibility(View.VISIBLE);
        }
        else {
            recipeDAO.getRandomRecipe(new ResponseListener<RecipePreview>() {
                @Override
                public void onError(String message) {
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(RecipePreview response) {
                    randomRecipe = new RecipePreview(response.getId(),
                            response.getName(),  response.getImgURL());

                    setupRecipe(randomRecipe);
                    recipeCard.setVisibility(View.VISIBLE);
                    randomRecipeText.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void setupRecipe(RecipePreview recipe) {
        Picasso.get().load(recipe.getImgURL()).resize(300,300).
                centerCrop().into(recipeImg);
        recipeNameText.setText(recipe.getName());

        btnViewRecipe.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RecipeActivity.class);

            intent.putExtra("RECIPE_ID", recipe.getId());
            intent.putExtra("RECIPE_NAME", recipe.getName());
            intent.putExtra("RECIPE_IMG", recipe.getImgURL());
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

            startActivity(intent);
        });
    }

    private void setupCategories() {
        FlexboxLayout categoryLayout = findViewById(R.id.categoryLayout);
        categoryLayout.setVisibility(View.INVISIBLE);
        TextView categoryTitle = findViewById(R.id.categoryTitle);
        categoryTitle.setVisibility(View.INVISIBLE);

        recipeDAO.getCategories(new ResponseListener<List<String>>() {
            @Override
            public void onError(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(List<String> response) {
                for (String category: response) {
                    TextView textView = getTextView(category);
                    categoryLayout.addView(textView);
                    textView.setOnClickListener(v -> createSearchRequest(SearchType.CATEGORY, category));
                }
                categoryLayout.setVisibility(View.VISIBLE);
                categoryTitle.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupAreas() {
        FlexboxLayout areaLayout = findViewById(R.id.areaLayout);
        areaLayout.setVisibility(View.INVISIBLE);
        TextView areaTitle = findViewById(R.id.areaTitle);
        areaTitle.setVisibility(View.INVISIBLE);

        recipeDAO.getAreas(new ResponseListener<List<String>>() {
            @Override
            public void onError(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(List<String> response) {
                for (String area: response) {
                    TextView textView = getTextView(area);
                    areaLayout.addView(textView);
                    textView.setOnClickListener(v -> createSearchRequest(SearchType.AREA, area));
                }
                areaLayout.setVisibility(View.VISIBLE);
                areaTitle.setVisibility(View.VISIBLE);
            }
        });
    }
}