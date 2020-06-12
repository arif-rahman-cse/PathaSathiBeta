package com.example.pathasathi.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.pathasathi.MySingleton;
import com.example.pathasathi.R;
import com.example.pathasathi.activity.AvailablePsActivity;
import com.example.pathasathi.activity.CurrentLocationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment implements View.OnClickListener {
    private static final String TAG = "Home";

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAA7lzCl5U:APA91bF1S7cUxauyPEVZBdNsIf12mdL4Fu9JaMkRNXnt7FMAyWavQfGZBLLuCJGeyAEWGZarmjyEN525vyuRNn270bnbKyT_fL1lcRDp58hkHOEpKTeQ1cNGqCOPPdafspvANO2JMJlz";
    final private String contentType = "application/json";
    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;

    private static final int REQUEST_CALL = 1;
    private ImageView myPathasathi, currentLocation, anyOneThere, help, call, markAsAsafe;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        FirebaseMessaging.getInstance().subscribeToTopic("/topics/userABC");

        myPathasathi = view.findViewById(R.id.my_pathasati);
        currentLocation = view.findViewById(R.id.current_location);
        anyOneThere = view.findViewById(R.id.anyone_there);
        call = view.findViewById(R.id.call);
        help = view.findViewById(R.id.help);

        currentLocation.setOnClickListener(this);
        anyOneThere.setOnClickListener(this);
        call.setOnClickListener(this);
        help.setOnClickListener(this);

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
                popUpCallDialog(v);
                break;

            case R.id.help:
                notifyNearByUser(v);


        }

    }

    private void notifyNearByUser(final View v) {
        Log.d(TAG, "notifyNearByUser: Clicked");

        //Push Notification title message
        TOPIC = "/topics/userABC"; //topic has to match what the receiver subscribed to
        NOTIFICATION_TITLE = "Help Me";
        NOTIFICATION_MESSAGE = "I am in danger ⚠";

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", NOTIFICATION_TITLE);
            notifcationBody.put("message", NOTIFICATION_MESSAGE);

            notification.put("to", TOPIC);
            notification.put("data", notifcationBody);
        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Nearby User Notified", Snackbar.LENGTH_LONG).show();
                        //Toast.makeText(getContext(), "Nearby User Notified", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onResponse: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);

    }

    private void popUpCallDialog(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
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

        final String number = "01624390843";
        String zipCode = "1208";

        DocumentReference docRef = db.collection("EmergencyNumber").document(zipCode);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot dsf = task.getResult();

                    if (dsf != null && dsf.exists()) {

                        String num1 = dsf.getString("number1");
                        String num2 = dsf.getString("number2");

                        Log.d(TAG, "numbers: " + num1 + " " + num2);

                        assert num1 != null;
                        if (num1.trim().length() > 0) {

                            if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                            } else {
                                String dial = "tel:" + num1;
                                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                            }

                        } else {
                            Toast.makeText(getContext(), "Enter Phone Number", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        Log.d(TAG, "makePhoneCall : onComplete : No document");
                    }

                } else {
                    Log.d(TAG, "makePhoneCall called : Failed");
                }
            }
        });
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
