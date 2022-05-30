package com.example.pathasathi.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.pathasathi.R;
import com.example.pathasathi.RecyclerViewItemClickListener;
import com.example.pathasathi.adapters.MyPathasathiAdapter;
import com.example.pathasathi.adapters.SearchPathasathiAdapter;
import com.example.pathasathi.databinding.ActivityMyPathaSathiBinding;
import com.example.pathasathi.model.MyPathasathi;
import com.example.pathasathi.model.SearchPathasathi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MyPathaSathi extends AppCompatActivity implements RecyclerViewItemClickListener {
    private static final String TAG = "MyPathaSathi";

    private ActivityMyPathaSathiBinding myPathaSathiBinding;
    private MyPathasathiAdapter myPathasathiAdapter;
    private ArrayList<MyPathasathi> myPathasathiArrayList = new ArrayList<>();
    private GridLayoutManager mLayoutManager;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MyPathasathi myPathasathi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_patha_sathi);

        myPathaSathiBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_patha_sathi);

        TextView title = findViewById(R.id.title_tv);
        title.setText(getString(R.string.my_patha_sathi));

        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mLayoutManager = new GridLayoutManager(this, 2);
        myPathasathiAdapter = new MyPathasathiAdapter(this, myPathasathiArrayList, this);
        myPathaSathiBinding.myPathasatiRv.setLayoutManager(mLayoutManager);
        myPathaSathiBinding.myPathasatiRv.setAdapter(myPathasathiAdapter);

        getMyPathasathi();
    }


    private void getMyPathasathi() {

        final CollectionReference docRef = db.collection(getString(R.string.collection_users));

        docRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            QuerySnapshot searchPathasathi = task.getResult();
                            if (searchPathasathi != null) {

                                // Clear the list and add all the users again
                                myPathasathiArrayList.clear();
                                myPathasathiArrayList = new ArrayList<>();

                                for (QueryDocumentSnapshot document : searchPathasathi) {

                                    String name = document.getString("name");
                                    Log.d(TAG, "onComplete: Pathasathi Name: " + name);

                                    addMyPathasathi(document);

                                }

                                setsearchPathasathiOnRv(getApplicationContext(), myPathasathiArrayList);
                            }
                        } else {
                            Log.d(TAG, "onComplete: Error getting documents: " + task.getException());
                        }

                    }
                });

    }

    private void setsearchPathasathiOnRv(Context applicationContext, ArrayList<MyPathasathi> myPathasathiArrayList) {

        if (myPathasathiArrayList.size() != 0) {
            Log.d(TAG, "setsearchPathasathiOnRv: Pathasathi ArrayList is not empty");
            mLayoutManager = new GridLayoutManager(this, 2);
            myPathasathiAdapter = new MyPathasathiAdapter(this, myPathasathiArrayList, this);
            myPathaSathiBinding.myPathasatiRv.setLayoutManager(mLayoutManager);
            myPathaSathiBinding.myPathasatiRv.setAdapter(myPathasathiAdapter);
        } else {
            Log.d(TAG, "setOnlineDoctorsOnRv:Pathasathi ArrayList is empty");
        }
    }

    private void addMyPathasathi(QueryDocumentSnapshot document) {
        myPathasathi = new MyPathasathi(
                document.getString("name"),
                document.getString("avatar"),
                document.getString("user_id"));

        myPathasathiArrayList.add(myPathasathi);

    }

    @Override
    public void didPressed(String id) {

    }
}
