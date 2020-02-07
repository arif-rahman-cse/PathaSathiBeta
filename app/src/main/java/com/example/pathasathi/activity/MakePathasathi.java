package com.example.pathasathi.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.pathasathi.R;
import com.example.pathasathi.databinding.ActivityMakePathasathiBinding;
import com.example.pathasathi.fragment.AllPathaSathiFragment;
import com.example.pathasathi.fragment.PSSuggestionFragment;

public class MakePathasathi extends AppCompatActivity {
    private static final String TAG = "MakePathasathi";

    private ActivityMakePathasathiBinding mBinding;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_pathasathi);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_make_pathasathi);

        title = findViewById(R.id.title_tv);

        title.setText(getString(R.string.make_pathasathi));

        mBinding.suggestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                Log.d(TAG, "onClick: suggestion button clicked");
                replaceFragment(new PSSuggestionFragment());
            }
        });

        mBinding.allPathasathiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new AllPathaSathiFragment());
            }
        });




    }

    private void replaceFragment(Fragment fragment) {
        Log.d(TAG, "replaceFragment: called");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.make_pathasathi_container, fragment);
        fragmentTransaction.commit();
    }
}
