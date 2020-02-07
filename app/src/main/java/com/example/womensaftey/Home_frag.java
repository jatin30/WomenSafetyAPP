package com.example.womensaftey;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Home_frag extends Fragment implements View.OnClickListener {

    private Boolean alreadyClicked = false;
    //        LocationRequest locationRequest;
//    FusedLocationProviderClient fusedLocationClient;
    public static final String MY_PREFS_FILENAME = "com.example.womensaftey.myfile";


    private Context context;
    private TextView alrttxt;
    private ImageView alrtimg;
    private Button btncncl;
    View view;


    Home_frag(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.homefrag_layout, container, false);
        alrtimg = view.findViewById(R.id.alrtimg);
        alrttxt = view.findViewById(R.id.alrttxtx);
        btncncl = view.findViewById(R.id.btncncl);
        alrtimg.setOnClickListener(this);
        btncncl.setOnClickListener(this);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences preferences = context.getSharedPreferences(MY_PREFS_FILENAME, Context.MODE_PRIVATE);
        alreadyClicked = preferences.getBoolean("check", false);

        if (alreadyClicked) {
            alrttxt.setVisibility(View.VISIBLE);
            btncncl.setEnabled(true);
            btncncl.setVisibility(View.VISIBLE);
        } else if (!alreadyClicked) {
            btncncl.setEnabled(false);
            btncncl.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = context.
                getSharedPreferences(MY_PREFS_FILENAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean("check", alreadyClicked);
        editor.commit();
    }


    public void sendLinkTOguardians() {


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.alrtimg:
                alrtimgCLICKED();
                break;

            case R.id.btncncl:
                cnclBtnCLICKED();
                break;
        }
    }


    private void alrtimgCLICKED() {

        if (alreadyClicked == false) {

            alreadyClicked = true;
            alrttxt.setVisibility(View.VISIBLE);
            btncncl.setEnabled(true);
            btncncl.setVisibility(View.VISIBLE);

            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("message");
            mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("canCheck").setValue(true);

            sendLinkTOguardians();

            sendLocationUpdates();
        }

    }

    private void sendLocationUpdates() {

        //OneTimeWorkRequest request=new OneTimeWorkRequest.Builder(LocationWorkManager.class).addTag("locationRequest").build();

        //WorkManager.getInstance(context).enqueue(request);

        Intent intent = new Intent(context, LocationUpdateService.class);
        context.startService(intent);
    }

    private void cnclBtnCLICKED() {


        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("message");
        mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("canCheck").setValue(false);

        btncncl.setEnabled(false);
        btncncl.setVisibility(View.GONE);
        alreadyClicked = false;
        alrttxt.setVisibility(View.INVISIBLE);

        //fusedLocationClient.removeLocationUpdates(locationCallback);
        //LocationWorkManager.onStopped0();
        //WorkManager.getInstance(context).cancelAllWorkByTag("locationRequest");

        Intent intent = new Intent(context, LocationUpdateService.class);
        context.stopService(intent);
    }


//    public LocationCallback locationCallback = new LocationCallback() {
//        @Override
//        public void onLocationResult(LocationResult locationResult) {
//            super.onLocationResult(locationResult);
//            Log.d(TAG, "onLocationResult: loc Changed");
//
//            LocationUpdateTask locationUpdateTask = new LocationUpdateTask(locationResult);
//            locationUpdateTask.execute();
//
//        }
//    };

//    public void sendLocationUpdates() {
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
//        createLocationRequest();
//
//        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
//    }


//    protected void createLocationRequest() {
//        locationRequest = LocationRequest.create();
//        locationRequest.setInterval(10000);
//        locationRequest.setFastestInterval(5000);
//        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//    }

//    public class LocationUpdateTask extends AsyncTask<Void, Void, Void> {
//        LocationResult locationResult;
//
//        public LocationUpdateTask(LocationResult locationResult) {
//            this.locationResult = locationResult;
//        }
//
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            LatLng latLng = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
//            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("message");
//            mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("latLng").setValue(latLng);
//            return null;
//        }
//    }


    //    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        alrtimg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//    }

}


