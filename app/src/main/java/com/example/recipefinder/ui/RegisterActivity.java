package com.example.recipefinder.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recipefinder.R;
import com.example.recipefinder.db.UserDAO;
import com.example.recipefinder.model.User;

public class RegisterActivity extends AppCompatActivity {
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        userDAO = new UserDAO(this);

        Button registerButton = findViewById(R.id.registerButton);
        TextView usernameField = findViewById(R.id.username);
        TextView emailField = findViewById(R.id.email);
        TextView passwordField = findViewById(R.id.password);
        TextView confirmPasswordField = findViewById(R.id.confirmPassword);

        registerButton.setOnClickListener(view -> {
            // Get user inputs
            String username = usernameField.getText().toString();
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();
            String confirmPassword = confirmPasswordField.getText().toString();

            // Validate the inputs
            if (validateInputs(username, email, password, confirmPassword)) {
                // Add the user to the database
                userDAO.addUser(new User(username, email, password));
                // Proceed with registration (e.g., save data to database, etc.)
                Toast.makeText(RegisterActivity.this, "Registration Successful!",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private boolean validateInputs(String username, String email, String password,
                                   String confirmPassword) {
        boolean isIncomplete = username.isEmpty() || email.isEmpty() ||
                password.isEmpty() || confirmPassword.isEmpty();

        if (isIncomplete) {
            Toast.makeText(RegisterActivity.this, "Please fill in all fields",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(RegisterActivity.this, "Passwords do not match",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if (userDAO.getUser(username) != null) {
            Toast.makeText(RegisterActivity.this, "User already exists",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
