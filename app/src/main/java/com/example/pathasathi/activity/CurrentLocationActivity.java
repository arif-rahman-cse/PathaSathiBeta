package com.example.pathasathi.activity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.pathasathi.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class CurrentLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "CurrentLocationActivity";

    //Static Field
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 121;
    private static final float DEFAULT_ZOOM = 15f;
    private static final int LOCATION_SETTINGS_REQUEST = 101;

    //Vars
    private GoogleMap mMap;
    private Boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location);

        getLocationPermission();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: map is ready");
        Toast.makeText(this, "Map is ready!", Toast.LENGTH_SHORT).show();
        mMap = googleMap;

        // Add a marker in my location and move the camera
        if (mLocationPermissionGranted) {
            Log.d(TAG, "onMapReady : getDeviceLocation method is calling");
            getDeviceLocation();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
        }
    }

    //-------------------------------------------- get device current location---------------------------//
    private void getDeviceLocation() {

        Log.d(TAG, "getDeviceLocation: Getting device Current Location");

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionGranted) {
                final Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Log.d(TAG, "onSuccess:");
                        if (location != null) {
                            Log.d(TAG, "Location is not null");
                            moveCamera(new LatLng(location.getLatitude(), location.getLongitude()), DEFAULT_ZOOM, "My Location");
                        } else {
                            Log.d(TAG, "Location is null");
                        }
                    }
                });
            }

        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    //-----------------------------Move camera to user current location---------------------------------//
    private void moveCamera(LatLng latLng, float zoom, String my_location) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + "lag: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if (!my_location.equals("My Location")) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .title(my_location);
            mMap.addMarker(markerOptions);
        }
    }

    //------------------------------ get User location permission ------------------------------//
    private void getLocationPermission() {

        Log.d(TAG, "getLocationPermission: Getting location permission");
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getLocationPermission: FINE_LOCATION permission PERMISSION_GRANTED");

            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "getLocationPermission: COURSE_LOCATION permission PERMISSION_GRANTED");
                mLocationPermissionGranted = true;
                inintMap();
            } else {
                Log.d(TAG, "getLocationPermission: App needs Location permission");
                ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            Log.d(TAG, "getLocationPermission: App needs Location permission");
            ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQUEST_CODE);
        }

    }


    //-------------------------------- initializing map nad map is ready to be used ----------------------------------------//
    private void inintMap() {

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.user_current_location);
        mapFragment.getMapAsync(this);

    }
}
