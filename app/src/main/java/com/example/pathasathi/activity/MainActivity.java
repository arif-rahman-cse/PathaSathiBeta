package com.example.pathasathi.activity;

import androidx.annotation.NonNull;

import com.example.pathasathi.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private BottomNavigationView bottomNavigationView;
    private DrawerLayout drawerLayout;
    private NavigationView drawerNavigationView;
    private ImageView expandedMenu;
    private TextView userEmail, name;

    private UserDetails userDetails;
    private Home home;
    private Notification notification;
    private AmarPathaSathi amarPathaSathi;
    private Move move;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                //get drawer data
                getUserDrawerData();
            }
        });


    }


    //----------------------------------- get user drawer data from fire base -----------------------------------------//
    private void getUserDrawerData() {
        Log.d(TAG, "getUserDrawerData called:");

        //drawer data
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
                                    documentSnapshot.getString("name") +" \n" + documentSnapshot.getString("email"));

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
