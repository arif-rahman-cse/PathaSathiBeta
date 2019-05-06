package com.example.pathasathi.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;


import com.example.pathasathi.R;
import com.example.pathasathi.fragment.AmarPathaSathi;
import com.example.pathasathi.fragment.Home;
import com.example.pathasathi.fragment.Move;
import com.example.pathasathi.fragment.Notification;
import com.example.pathasathi.fragment.UserDetails;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private UserDetails userDetails;
    private Home home;
    private Notification notification;
    private AmarPathaSathi amarPathaSathi;
    private Move move;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Remove Action Bar

        //Create Object of Fragment
        userDetails = new UserDetails();
        home = new Home();
        notification = new Notification();
        amarPathaSathi = new AmarPathaSathi();
        move = new Move();

        bottomNavigationView = findViewById(R.id.bottom_navigation_id);

        //First call Home Fragment
        replaceFragment(home);

        //Set Listener on Bottom navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId())
                {
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

                    default:
                        return false;
                }


            }
        });
    }

    //Just replace the one fragment to another fragment
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();

    }
}
