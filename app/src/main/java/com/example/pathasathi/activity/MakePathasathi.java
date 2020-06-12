package com.example.pathasathi.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.pathasathi.R;
import com.example.pathasathi.RecyclerViewItemClickListener;
import com.example.pathasathi.adapters.SearchPathasathiAdapter;
import com.example.pathasathi.databinding.ActivityMakePathasathiBinding;
import com.example.pathasathi.model.SearchPathasathi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MakePathasathi extends AppCompatActivity implements RecyclerViewItemClickListener {
    private static final String TAG = "MakePathasathi";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private SearchPathasathi searchPathasathi;
    private ArrayList<SearchPathasathi> searchPathasathiArrayList;
    private SearchPathasathiAdapter searchPathasathiAdapter;
    private ActivityMakePathasathiBinding mBinding;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_pathasathi);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_make_pathasathi);
        title = findViewById(R.id.title_tv);
        title.setText(getString(R.string.make_pathasathi));

        searchPathasathiArrayList = new ArrayList<>();

        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                onBackPressed();
            }
        });

        mBinding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getSearchedPathasathi(query);
                hideSoftKeyboard();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });


    }

    private void getSearchedPathasathi(String query) {
        Log.d(TAG, "getSearchedProduct: Query Text: " + query);

        final CollectionReference docRef = db.collection(getString(R.string.collection_users));

        docRef.whereEqualTo("name", query)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            QuerySnapshot searchPathasathi = task.getResult();
                            if (searchPathasathi != null) {

                                for (QueryDocumentSnapshot document : searchPathasathi) {

                                    String name = document.getString("name");
                                    Log.d(TAG, "onComplete: Pathasathi Name: " + name);

                                    addSearchPathasathi(document);

                                }

                                setsearchPathasathiOnRv(getApplicationContext(), searchPathasathiArrayList);
                            }
                        } else {
                            Log.d(TAG, "onComplete: Error getting documents: " + task.getException());
                        }

                    }
                });

    }

    private void setsearchPathasathiOnRv(Context applicationContext, ArrayList<SearchPathasathi> searchPathasathiArrayList) {

        if (searchPathasathiArrayList.size() != 0) {
            Log.d(TAG, "setOnlineDoctorsOnRv: onlineDoctorsArrayList is not empty");
            searchPathasathiAdapter = new SearchPathasathiAdapter(applicationContext, searchPathasathiArrayList, this);
            mBinding.searchPathasathiRv.setLayoutManager(new LinearLayoutManager(MakePathasathi.this));
            mBinding.searchPathasathiRv.setHasFixedSize(true);
            mBinding.searchPathasathiRv.setAdapter(searchPathasathiAdapter);
        } else {
            Log.d(TAG, "setOnlineDoctorsOnRv: onlineDoctorsArrayList is empty");
        }
    }

    private void addSearchPathasathi(QueryDocumentSnapshot document) {

        searchPathasathi = new SearchPathasathi(
                document.getString("name"),
                document.getString("avatar"),
                document.getString("user_id"));

        searchPathasathiArrayList.add(searchPathasathi);
    }

    @Override
    public void didPressed(String id) {
        Log.d(TAG, "didPressed: ID" + id);
    }

    private void hideSoftKeyboard() {

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
