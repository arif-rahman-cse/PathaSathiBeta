package com.example.pathasathi.fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pathasathi.R;
import com.example.pathasathi.adapters.NotificationAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class Notification extends Fragment {
    private RecyclerView recyclerView;
    private String[] notificationList;


    public Notification() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        recyclerView = view.findViewById(R.id.notification_recycle_view);
        notificationList = getResources().getStringArray(R.array.notification_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        NotificationAdapter adapter=new NotificationAdapter(getActivity(),notificationList);
        recyclerView.setAdapter(adapter);


        return view;
    }

}
