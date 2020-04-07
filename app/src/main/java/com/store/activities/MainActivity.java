package com.store.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationView;
import com.store.R;
import com.store.fragments.CategoriesListFragment;
import com.store.fragments.LoginFragment;
import com.store.fragments.ProductListFragment;
import com.store.fragments.SearchFragment;
import com.store.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Setarea preferintelor
        setupSharedPreferences();

        //Setarea toolbar-ului
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);

        //Setarea meniului
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProductListFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_products);
        }

        //Setarea altor butoane de pe toolbar
        Button toolBarBtn = findViewById(R.id.loginButton);
        toolBarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog("login");
            }
        });

        ImageButton searchBtn = findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog("search");
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent intent;
        switch (menuItem.getItemId()) {
            case R.id.nav_products:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProductListFragment()).commit();
                break;
            case R.id.nav_categories:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CategoriesListFragment()).commit();
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                break;
            case R.id.nav_sensors:
                intent = new Intent(this, SensorsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_location:
                intent = new Intent(this, LocationActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_camera:
                intent = new Intent(this, CameraActivity.class);
                startActivity(intent);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openDialog(String dialogType) {
        if (dialogType.equals("login")) {
            LoginFragment loginFragment = new LoginFragment();
            loginFragment.show(getSupportFragmentManager(), "login");
        } else if (dialogType.equals("search")) {
            SearchFragment searchFragment = new SearchFragment();
            searchFragment.show(getSupportFragmentManager(), "search");
        }
    }

    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        changeToolbarColor(sharedPreferences);
        changeToolbarOpacity(sharedPreferences);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("toolbar_color"))
            changeToolbarColor(sharedPreferences);
        else if (key.equals("toolbar_opacity"))
            changeToolbarOpacity(sharedPreferences);
    }

    private void changeToolbarOpacity(SharedPreferences preferences) {
        Toolbar toolbar = findViewById(R.id.toolbar);

        try {
            int value = Integer.parseInt(preferences.getString("toolbar_opacity", "12"));
            toolbar.getBackground().setAlpha(value);
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.error_not_number,
                    Toast.LENGTH_LONG).show();
        }

    }

    private void changeToolbarColor(SharedPreferences preferences) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        switch (preferences.getString("toolbar_color", "no_color")) {
            case "red":
                toolbar.setBackgroundColor(Color.RED);
                break;
            case "green":
                toolbar.setBackgroundColor(Color.GREEN);
                break;
            case "blue":
                toolbar.setBackgroundColor(Color.BLUE);
                break;
            default:
                toolbar.setBackgroundResource(R.color.colorPrimaryDark);
                break;
        }


    }
}
