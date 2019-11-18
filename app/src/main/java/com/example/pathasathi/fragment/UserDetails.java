package com.example.pathasathi.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pathasathi.R;

import static android.app.Activity.RESULT_OK;


public class UserDetails extends Fragment implements View.OnClickListener {

    private EditText editTextEmail, editTextMobile, editTextJoind, editTextAddress, editTextCity,
            editTextCountry, editTextBirthDay;

    private ImageView imageViewProfileEdit, imageViewEmailEditSave, imageViewTakePhoto, imageViewProfileImage;

    private static final int PICK_FROM_GALLERY = 0;
    private  static final int PICK_FROM_CAMERA = 1;

    public UserDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_details, container, false);

        // Find 4 image view editIconImage, saveIconImage, profileImage, ProfileImageShow.
        imageViewProfileEdit = view.findViewById(R.id.editProfile);
        imageViewEmailEditSave = view.findViewById(R.id.saveEditProfile);
        imageViewProfileImage = view.findViewById(R.id.prifileImageId);
        imageViewTakePhoto = view.findViewById(R.id.takePhotoId);

        // Find Profile fields
        editTextEmail = view.findViewById(R.id.emailFieldId);
        editTextMobile = view.findViewById(R.id.mobilefieldId);
        editTextJoind = view.findViewById(R.id.pathshathiJointFieldId);
        editTextAddress = view.findViewById(R.id.addressFieldId);
        editTextCity = view.findViewById(R.id.cityFieldId);
        editTextCountry = view.findViewById(R.id.countryFieldId);
        editTextBirthDay = view.findViewById(R.id.birthDayFieldId);

        // Set onclick listener on EditIcon, SaveIcon, TakePhotoIcon.
        imageViewProfileEdit.setOnClickListener(this);
        imageViewEmailEditSave.setOnClickListener(this);
        imageViewTakePhoto.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.editProfile){

            editTextEmail.setFocusableInTouchMode(true);
            editTextEmail.setFocusable(true);
            editTextEmail.setSelection(editTextEmail.getText().length());

            editTextMobile.setFocusableInTouchMode(true);
            editTextMobile.setFocusable(true);
            editTextMobile.setSelection(editTextMobile.getText().length());

            editTextJoind.setFocusableInTouchMode(true);
            editTextJoind.setFocusable(true);
            editTextJoind.setSelection(editTextJoind.getText().length());

            editTextAddress.setFocusableInTouchMode(true);
            editTextAddress.setFocusable(true);
            editTextAddress.setSelection(editTextAddress.getText().length());

            editTextCity.setFocusableInTouchMode(true);
            editTextCity.setFocusable(true);
            editTextCity.setSelection(editTextCity.getText().length());

            editTextCountry.setFocusableInTouchMode(true);
            editTextCountry.setFocusable(true);
            editTextCountry.setSelection(editTextCountry.getText().length());

            editTextBirthDay.setFocusableInTouchMode(true);
            editTextBirthDay.setFocusable(true);
            editTextBirthDay.setSelection(editTextBirthDay.getText().length());


            imageViewEmailEditSave.setVisibility(View.VISIBLE);
            imageViewProfileEdit.setVisibility(View.GONE);

            Toast.makeText(getActivity(), "Edit Your Profile",
                    Toast.LENGTH_SHORT).show();
        }

        else if (v.getId() == R.id.saveEditProfile){

            editTextEmail.setFocusable(false);
            editTextMobile.setFocusable(false);
            editTextJoind.setFocusable(false);
            editTextAddress.setFocusable(false);
            editTextCity.setFocusable(false);
            editTextCountry.setFocusable(false);
            editTextBirthDay.setFocusable(false);

            imageViewEmailEditSave.setVisibility(View.GONE);
            imageViewProfileEdit.setVisibility(View.VISIBLE);

            Toast.makeText(getContext(), "Save Your Data",
                    Toast.LENGTH_SHORT).show();
        }

        else if (v.getId() == R.id.takePhotoId){

            OptionDialog();
            Toast.makeText(getActivity(), "take your photo",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void OptionDialog() {
        final String [] option ={"Take Picture From Gallery","Take Picture by Camera "};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Option");
        builder.setSingleChoiceItems(option, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(which == 0)
                {
                    openGallary();

                }
                if(which == 1)
                {

                    openCamera();

                }

                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void openGallary() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_FROM_GALLERY);
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, PICK_FROM_CAMERA);
            Toast.makeText(getActivity(), "Camera is Calling", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FROM_CAMERA && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageViewProfileImage.setImageBitmap(imageBitmap);
        }

        else if(requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK){
            Uri selectedImage = data.getData();
            imageViewProfileImage.setImageURI(selectedImage);
            Toast.makeText(getActivity(), "Gallery is Calling", Toast.LENGTH_SHORT).show();

        }

    }
}
