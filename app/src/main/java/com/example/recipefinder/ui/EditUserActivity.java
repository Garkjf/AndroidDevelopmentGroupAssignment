package com.example.recipefinder.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipefinder.R;
import com.example.recipefinder.db.UserDAO;
import com.example.recipefinder.model.User;
import com.example.recipefinder.utils.UserSession;

public class EditUserActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText oldPasswordEditText;
    private EditText newPasswordEditText;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        userDAO = new UserDAO(this);
        usernameEditText = findViewById(R.id.edit_username);
        emailEditText = findViewById(R.id.edit_email);
        oldPasswordEditText = findViewById(R.id.edit_old_password);
        newPasswordEditText = findViewById(R.id.edit_password);
        Button saveButton = findViewById(R.id.btn_save);

        // Get the current user details
        User currentUser = UserSession.getInstance().getCurrentUser();
        if (currentUser != null) {
            usernameEditText.setText(currentUser.getUsername());
            emailEditText.setText(currentUser.getEmail());
        }

        saveButton.setOnClickListener(v -> saveUserDetails());
    }

    private void saveUserDetails() {
        String newUsername = usernameEditText.getText().toString().trim();
        String newEmail = emailEditText.getText().toString().trim();
        String oldPassword = oldPasswordEditText.getText().toString().trim();
        String newPassword = newPasswordEditText.getText().toString().trim();

        // Validation checks for mandatory fields
        if (newUsername.isEmpty() || newEmail.isEmpty()) {
            Toast.makeText(this, "Username and Email fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        User currentUser = UserSession.getInstance().getCurrentUser();
        if (currentUser != null) {
            // If the user wants to change the password, check the old password
            if (!oldPassword.isEmpty() || !newPassword.isEmpty()) {

                if (oldPassword.isEmpty() || newPassword.isEmpty()) {
                    Toast.makeText(this, "Please fill both old and new passwords to update the password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!currentUser.getPassword().equals(oldPassword)) {
                    Toast.makeText(this, "Old password is incorrect", Toast.LENGTH_SHORT).show();
                    return;
                }

                currentUser.setPassword(newPassword);
            }

            currentUser.setUsername(newUsername);
            currentUser.setEmail(newEmail);

            userDAO.editUser(currentUser.getId(), currentUser);

            // Update session
            UserSession.getInstance().setCurrentUser(currentUser);

            Toast.makeText(this, "User details updated", Toast.LENGTH_SHORT).show();

            finish();
        }
    }
}

