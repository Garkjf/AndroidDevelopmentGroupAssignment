package com.example.recipefinder;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.recipefinder.api.RecipeDAO;
import com.example.recipefinder.api.ResponseListener;
import com.example.recipefinder.ui.SearchResultActivity;
import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecipeDAO recipeDAO;
    FlexboxLayout categoryLayout, areaLayout;

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

                Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                intent.putExtra("SEARCH_TYPE", "query");
                intent.putExtra("SEARCH_TERM", searchTerm);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        recipeDAO = RecipeDAO.getInstance(this);
        setupCategories();
        setupAreas();
    }

    private void setupCategories() {
        categoryLayout = findViewById(R.id.categoryLayout);

        recipeDAO.getCategories(new ResponseListener<List<String>>() {
            @Override
            public void onError(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(List<String> response) {
                for (String category : response) {
                    TextView textView = new TextView(MainActivity.this);
                    textView.setText(category);
                    textView.setPadding(16, 16, 16, 16);
                    textView.setTextColor(Color.WHITE);
                    textView.setBackgroundResource(R.drawable.badge_background);

                    FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                            FlexboxLayout.LayoutParams.WRAP_CONTENT,
                            FlexboxLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(8, 8, 8, 8);
                    textView.setLayoutParams(params);

                    categoryLayout.addView(textView);

                    textView.setClickable(true); // Set clickable to true
                    textView.setFocusable(true); // Set focusable to true

                    // Set an OnClickListener
                    textView.setOnClickListener(v -> {
                        Intent intent = new Intent(MainActivity.this,
                                SearchResultActivity.class);
                        intent.putExtra("SEARCH_TYPE", "category");
                        intent.putExtra("SEARCH_TERM", category);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    });
                }
            }
        });
    }

    private void setupAreas() {
        areaLayout = findViewById(R.id.areaLayout);

        recipeDAO.getAreas(new ResponseListener<List<String>>() {
            @Override
            public void onError(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(List<String> response) {
                for (String area : response) {
                    TextView textView = new TextView(MainActivity.this);
                    textView.setText(area);
                    textView.setPadding(16, 16, 16, 16);
                    textView.setTextColor(Color.WHITE);
                    textView.setBackgroundResource(R.drawable.badge_background);

                    FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                            FlexboxLayout.LayoutParams.WRAP_CONTENT,
                            FlexboxLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(8, 8, 8, 8);
                    textView.setLayoutParams(params);

                    areaLayout.addView(textView);

                    textView.setClickable(true);
                    textView.setFocusable(true);

                    // Set an OnClickListener
                    textView.setOnClickListener(v -> {
                        Intent intent = new Intent(MainActivity.this,
                                SearchResultActivity.class);
                        intent.putExtra("SEARCH_TYPE", "area");
                        intent.putExtra("SEARCH_TERM", area);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    });
                }
            }
        });
    }
}