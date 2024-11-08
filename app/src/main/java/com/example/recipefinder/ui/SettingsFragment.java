package com.example.recipefinder.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.recipefinder.MainActivity;
import com.example.recipefinder.R;
import com.example.recipefinder.model.User;
import com.example.recipefinder.utils.UserSession;

public class SettingsFragment extends Fragment {

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        TextView usernameTextView = view.findViewById(R.id.name);
        TextView emailTextView = view.findViewById(R.id.email);
        Button changeUserDetailsButton = view.findViewById(R.id.btn_change_user_details);
        Button logoutButton = view.findViewById(R.id.btn_logout);

        // Get the current user from UserSession
        User currentUser = UserSession.getInstance().getCurrentUser();

        // Set the username and email TextViews
        if (currentUser != null) {
            usernameTextView.setText("Username: " + currentUser.getUsername());
            emailTextView.setText("Email: " + currentUser.getEmail());
        }
        
        changeUserDetailsButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditUserActivity.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(v -> {
            UserSession.getInstance().setCurrentUser(null);

            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        return view;
    }
}
