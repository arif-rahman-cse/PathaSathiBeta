package com.example.pathasathi.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pathasathi.R;
import com.example.pathasathi.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.squareup.picasso.Picasso;

import static android.text.TextUtils.isEmpty;
import static com.example.pathasathi.util.Check.doStringsMatch;
import static com.example.pathasathi.util.Config.PICK_IMAGE_REQUEST;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RegisterActivity";


    //widgets
    private EditText mName, mEmail, mPassword, mConfirmPassword;
    private ProgressBar mProgressBar;
    private Button registrationBtn;
    private ImageView backButton, addImage, profileImage;
    private TextView title;

    //vars
    private FirebaseFirestore mDb;
    private Uri profileImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mName = findViewById(R.id.input_name);
        mEmail = findViewById(R.id.input_email);
        mPassword = findViewById(R.id.input_password);
        mConfirmPassword = findViewById(R.id.input_confirm_password);
        mProgressBar = findViewById(R.id.progressBar);
        registrationBtn = findViewById(R.id.btn_register);
        backButton = findViewById(R.id.back_button);
        title = findViewById(R.id.title_tv);

        addImage = findViewById(R.id.add_profile_img_iv);
        profileImage = findViewById(R.id.user_profile_img_iv);


        title.setText(R.string.become_a_patha_sathi);

        registrationBtn.setOnClickListener(this);
        backButton.setOnClickListener(this);
        addImage.setOnClickListener(this);

        mDb = FirebaseFirestore.getInstance();

        hideSoftKeyboard();
    }

    //----------------------------------Register new user --------------------------//
    public void registerNewEmail(final String name, final String email, String password) {

        showDialog();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: AuthState: " + FirebaseAuth.getInstance().getCurrentUser().getUid());

                            //insert some default data
                            Users users = new Users();
                            users.setName(name);
                            users.setEmail(email);
                            users.setUsername(email);

                            //users.setUsername(email.substring(0, email.indexOf("@")));
                            users.setUser_id(FirebaseAuth.getInstance().getUid());

                            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().build();
                            mDb.setFirestoreSettings(settings);

                            DocumentReference newUserRef = mDb
                                    .collection(getString(R.string.collection_users))
                                    .document(FirebaseAuth.getInstance().getUid());

                            newUserRef.set(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    hideDialog();

                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                        redirectLoginScreen();
                                    } else {
                                        View parentLayout = findViewById(android.R.id.content);
                                        Snackbar.make(parentLayout, "Something went wrong.", Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {
                            View parentLayout = findViewById(android.R.id.content);
                            Snackbar.make(parentLayout, "Something went wrong.", Snackbar.LENGTH_SHORT).show();
                            hideDialog();
                        }

                        // ...
                    }
                });
    }

    //--------------Redirect Login screen ----------------------------------//
    private void redirectLoginScreen() {
        Log.d(TAG, "redirectLoginScreen: redirecting to login screen.");

        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    //------------------------------- Hide and show progress bar -----------------------------------//
    private void showDialog() {
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setIndeterminate(true);

    }

    private void hideDialog() {
        if (mProgressBar.isIndeterminate()) {
            mProgressBar.setVisibility(View.INVISIBLE);
            mProgressBar.setIndeterminate(false);
        }
    }

    private void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register: {
                Log.d(TAG, "onClick: attempting to register.");

                //check for null valued EditText fields
                if (!isEmpty(mName.getText().toString())
                        && !isEmpty(mEmail.getText().toString())
                        && !isEmpty(mPassword.getText().toString())
                        && !isEmpty(mConfirmPassword.getText().toString())) {

                    //check if passwords match
                    if (doStringsMatch(mPassword.getText().toString(), mConfirmPassword.getText().toString())) {

                        //Initiate registration task
                        registerNewEmail(mName.getText().toString(), mEmail.getText().toString(), mPassword.getText().toString());
                    } else {
                        Toast.makeText(RegisterActivity.this, "Passwords do not Match", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(RegisterActivity.this, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                }

                hideSoftKeyboard();
                break;
            }

            case R.id.back_button:{
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }

            case R.id.add_profile_img_iv:{
                Log.d(TAG, "Profile Image Add Button Clicked");
                openFileChooser();
            }
        }


    }

    //-------------------------------- set image in image view -----------------------------------------------//
    private void openFileChooser() {
        Log.d(TAG, "openFileChooser Clicked: ");
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK){

            profileImageUri = data.getData();
            Picasso.get().load(profileImageUri).into(profileImage);

        }
    }
}
