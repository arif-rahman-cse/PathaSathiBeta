package com.example.pathasathi.fragment;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.pathasathi.R;
import com.example.pathasathi.activity.CurrentLocationActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment implements View.OnClickListener{
    ImageView myPathasathi, currentLocation, anyOneThere, help, call, markAsAsafe;


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

        currentLocation.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(getContext(), CurrentLocationActivity.class));

    }
}
