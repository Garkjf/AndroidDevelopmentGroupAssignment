package com.example.recipefinder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.recipefinder.MainActivity;
import com.example.recipefinder.R;
import com.example.recipefinder.model.User;

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        
        Button changeUserDetailsButton = view.findViewById(R.id.btn_change_user_details);
        Button logoutButton = view.findViewById(R.id.btn_logout);

        // Handle "Change User Details" button click
        changeUserDetailsButton.setOnClickListener(v -> {
            // TODO: Implement user details change functionality
            // For now, it just shows a placeholder action
            // You may start a new activity or open a dialog for changing details
        });
        
        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        return view;
    }
}
