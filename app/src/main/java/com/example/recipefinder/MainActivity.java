package com.example.recipefinder;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.recipefinder.databinding.ActivityMainBinding;
import com.example.recipefinder.ui.FavouritesFragment;
import com.example.recipefinder.ui.HomeFragment;
import com.example.recipefinder.ui.SettingsFragment;
import com.example.recipefinder.ui.ShoppingListFragment;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    private HomeFragment homeFragment;
    private FavouritesFragment favouritesFragment;
    private ShoppingListFragment shoppingListFragment;
    private SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (savedInstanceState == null) {
            homeFragment = new HomeFragment();
            favouritesFragment = new FavouritesFragment();
            shoppingListFragment = new ShoppingListFragment();
            settingsFragment = new SettingsFragment();

            // Set the initial fragment
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout, homeFragment)
                    .commit();
        } else {
            homeFragment = (HomeFragment) getSupportFragmentManager()
                    .findFragmentByTag("home");
            favouritesFragment = (FavouritesFragment) getSupportFragmentManager()
                    .findFragmentByTag("favourites");
            shoppingListFragment = (ShoppingListFragment) getSupportFragmentManager()
                    .findFragmentByTag("shopping_list");
            settingsFragment = (SettingsFragment) getSupportFragmentManager()
                    .findFragmentByTag("settings");
        }

        replaceFragment(homeFragment);

        binding.bottomNavMenu.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.home) {
                replaceFragment(homeFragment);
            }
            else if (item.getItemId() == R.id.favourites) {
                replaceFragment(favouritesFragment);
            }
            else if (item.getItemId() == R.id.shopping_list) {
                replaceFragment(shoppingListFragment);
            }
            else if (item.getItemId() == R.id.settings) {
                replaceFragment(settingsFragment);
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}