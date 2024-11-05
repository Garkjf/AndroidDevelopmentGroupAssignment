package com.example.recipefinder2;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.recipefinder2.databinding.ActivityLoginBinding;
import com.example.recipefinder2.databinding.ActivityMainBinding;
import com.example.recipefinder2.ui.HomeActivity;
import com.example.recipefinder2.ui.RegisterActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up Register button click listener
        binding.registerButton.setOnClickListener(v -> {
            // Intent to navigate to RegisterActivity
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Set up Login button click listener (logic for login can be added here)
        binding.loginButton.setOnClickListener(v -> {

            String username = binding.username.getText().toString();
            String password = binding.password.getText().toString();

            if (!username.isEmpty() && !password.isEmpty()) {

                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                // Show an error message if fields are empty (add Toast or Snackbar)
            }
        });
    }
}
