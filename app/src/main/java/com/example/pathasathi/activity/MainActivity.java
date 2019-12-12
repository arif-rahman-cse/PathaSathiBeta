package com.example.pathasathi.activity;

import androidx.annotation.NonNull;

import com.example.pathasathi.model.Users;
import com.example.pathasathi.util.Config;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.pathasathi.R;
import com.example.pathasathi.fragment.AmarPathaSathi;
import com.example.pathasathi.fragment.Home;
import com.example.pathasathi.fragment.Move;
import com.example.pathasathi.fragment.Notification;
import com.example.pathasathi.fragment.UserDetails;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    //Wiz
    private static final String TAG = "MainActivity";
    private BottomNavigationView bottomNavigationView;
    private DrawerLayout drawerLayout;
    private NavigationView drawerNavigationView;
    private ImageView expandedMenu;

    //Vars
    private UserDetails userDetails;
    private Home home;
    private Notification notification;
    private AmarPathaSathi amarPathaSathi;
    private Move move;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FusedLocationProviderClient mFusedLocationClient;
    private List<Address> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        expandedMenu = findViewById(R.id.expanded_menu_iv);
        drawerLayout = findViewById(R.id.nav_drawer_id);
        bottomNavigationView = findViewById(R.id.bottom_navigation_id);
        drawerNavigationView = findViewById(R.id.drawer_navigation_id);

        //Create Object of Fragment
        userDetails = new UserDetails();
        home = new Home();
        notification = new Notification();
        amarPathaSathi = new AmarPathaSathi();
        move = new Move();

        //First call Home Fragment
        replaceFragment(home);

        //Get User Location Latitude and Longitude
        getLastLocation();


        //Set Listener on Bottom navigation
        if (bottomNavigationView != null) {
            bottomNavigationView.setOnNavigationItemSelectedListener(this);
        }
        if (drawerNavigationView != null) {
            drawerNavigationView.setNavigationItemSelectedListener(this);
        }


        expandedMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
                getUserDrawerData();
            }
        });


    }


    //------------------------------- Permission  for Google map  and getting User Location ----------------------------------------//

    private boolean checkSelfPermissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            return true;
        }
        return false;
    }

    private void requestPermissions(){
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                Config.LOCATION_PERMISSION_ID
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == Config.LOCATION_PERMISSION_ID){

            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // Granted. Start getting the location information
            }
        }
    }

    private boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkSelfPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    Log.d(TAG, "getLastLocation: onComplete :"+location.getLatitude());
                                    Log.d(TAG, "getLastLocation: onComplete :"+location.getLongitude());

                                    // User Lat Long
                                    double latitude = location.getLatitude();
                                    double longitude = location.getLongitude();

                                    //User Full Address
                                    Geocoder geocoder;
                                    geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

                                    try {
                                        // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                    String address = addresses.get(0).getAddressLine(0);
                                    String city = addresses.get(0).getLocality();
                                    String state = addresses.get(0).getAdminArea();
                                    String country = addresses.get(0).getCountryName();
                                    String postalCode = addresses.get(0).getPostalCode();
                                    String knownName = addresses.get(0).getFeatureName();

                                    Log.d(TAG, "getLastLocation: onComplete: "
                                            +address+"\t"+city+"\t"+state+"\t"+country+"\t"+postalCode+"\t"+knownName);

                                    //requestNewLocationData();
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(5000);
        //mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());

    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();

            Log.d(TAG, "requestNewLocationData: onLocationResult :"+mLastLocation.getLatitude());
            Log.d(TAG, "requestNewLocationData: onLocationResult :"+mLastLocation.getLongitude());
        }
    };


    //----------------------------------- get user drawer data from fire base -----------------------------------------//
    private void getUserDrawerData() {
        Log.d(TAG, "getUserDrawerData called:");

        final TextView userEmail, name;
        userEmail = findViewById(R.id.user_email_tv);
        name = findViewById(R.id.name_drawer_tv);

        String userId = FirebaseAuth.getInstance().getUid();
        Log.d(TAG, "getUserDrawerData called:" + userId);
        if (userId != null) {
            DocumentReference docRef = db.collection("Users").document(userId);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    Log.d(TAG, "getUserDrawerData called: onComplete");
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot != null && documentSnapshot.exists()) {

                            Log.d(TAG, "getUserDrawerData: onComplete: " +
                                    documentSnapshot.getString("name") + " \n" + documentSnapshot.getString("email"));

                            name.setText(documentSnapshot.getString("name"));
                            userEmail.setText(documentSnapshot.getString("email"));
                        } else {
                            Log.d(TAG, "getUserDrawerData : onComplete : No document");
                        }
                    } else {
                        Log.d(TAG, "getUserDrawerData called: Failed");
                    }
                }
            });
        }


    }


    //Just replace the one fragment to another fragment
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();

    }

    //------------------------------------- Navigation Item Selected Listener ---------------------------//
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            //Bottom Navigation item 
            case R.id.home_id:
                replaceFragment(home);
                return true;

            case R.id.profile_id:
                replaceFragment(userDetails);
                return true;

            case R.id.notification_id:
                replaceFragment(notification);
                return true;

            case R.id.myPathashati_id:
                replaceFragment(amarPathaSathi);
                return true;

            case R.id.move_id:
                replaceFragment(move);
                return true;

            //Drawer navigation item
            case R.id.log_out_id:
                logout();
                return true;

            case R.id.make_pathasathi_id:
                startActivity(new Intent(MainActivity.this, MakePathasathi.class));
                return true;

            default:
                return false;
        }
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        Toast.makeText(this, "Log Out", Toast.LENGTH_SHORT).show();
    }
}
