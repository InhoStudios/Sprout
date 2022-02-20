package com.joinsdn.sprout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.elevation.SurfaceColors;
import com.joinsdn.sprout.databinding.ActivityMainBinding;
import com.joinsdn.sprout.model.User;
import com.joinsdn.sprout.services.ProximityService;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    public static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // create user
        String firstName = "andy";
        String lastName = "zhao";
        String pfp = "pfp";
        String bio = "fat";
        user = new User(firstName, lastName, pfp, bio, this);

        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setNavigationBarColor(SurfaceColors.SURFACE_2.getColor(this));
        getWindow().setStatusBarColor(SurfaceColors.SURFACE_0.getColor(this));

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
//                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

//        Context context = getApplicationContext();
//        Intent proxyIntent = new Intent(this, ProximityService.class);
//        context.startForegroundService(proxyIntent);

        if (!hasPermissions(this, ProximityService.REQUIRED_PERMISSIONS)) {
            requestPermissions(ProximityService.REQUIRED_PERMISSIONS, ProximityService.REQUEST_CODE_REQUIRED_PERMISSIONS);
        }

    }

    private static boolean hasPermissions(Context context, String... permissions) {
        for (String permission: permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}