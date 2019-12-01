package com.example.pathasathi.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pathasathi.R;
import com.example.pathasathi.activity.AvailablePsActivity;
import com.example.pathasathi.activity.CurrentLocationActivity;
import com.google.android.material.button.MaterialButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment implements View.OnClickListener {

    private static final int REQUEST_CALL = 1;
    private ImageView myPathasathi, currentLocation, anyOneThere, help, call, markAsAsafe;


    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        myPathasathi = view.findViewById(R.id.my_pathasati);
        currentLocation = view.findViewById(R.id.current_location);
        anyOneThere = view.findViewById(R.id.anyone_there);
        call = view.findViewById(R.id.call);

        currentLocation.setOnClickListener(this);
        anyOneThere.setOnClickListener(this);
        call.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {
            case R.id.current_location:
                startActivity(new Intent(getContext(), CurrentLocationActivity.class));
                break;

            case R.id.anyone_there:
                startActivity(new Intent(getContext(), AvailablePsActivity.class));
                break;

            case R.id.call:
                popUpDialog(v);
                break;

        }

    }

    private void popUpDialog(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        ViewGroup viewGroup = view.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.emergency_call_dailog, viewGroup, false);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        //vars
        MaterialButton dismiss;
        TextView callPolice, callFireStation, callRab, callAmbulance, callFriends, callEmergency;

        callPolice = dialogView.findViewById(R.id.call_police_tv);
        dismiss = dialogView.findViewById(R.id.dismiss_bt);

        callPolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
                alertDialog.dismiss();
            }
        });


        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }


    private void makePhoneCall() {

        String number = "01624390843";
        if (number.trim().length() > 0) {

            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }

        } else {
            Toast.makeText(getContext(), "Enter Phone Number", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(getContext(), "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
