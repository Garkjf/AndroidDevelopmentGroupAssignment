package com.example.recipefinder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recipefinder.db.UserDAO;
import com.example.recipefinder.model.User;
import com.example.recipefinder.ui.HomeActivity;
import com.example.recipefinder.ui.RegisterActivity;
import com.example.recipefinder.utils.UserSession;

public class MainActivity extends AppCompatActivity {
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        userDAO = new UserDAO(this);
        Button registerButton = findViewById(R.id.registerButton);
        Button loginButton = findViewById(R.id.loginButton);
        TextView emailField = findViewById(R.id.email);
        TextView passwordField = findViewById(R.id.password);

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Set up Login button click
        loginButton.setOnClickListener(v -> {
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter email and password",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            User user = userDAO.getUser(email);
            if (user == null || !user.getPassword().equals(password)) {
                Toast.makeText(MainActivity.this, "Invalid email or password",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            UserSession.getInstance().setCurrentUser(user);
            startActivity(intent);
        });
    }
}
