package com.example.womensaftey;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;

public class Home_frag extends Fragment implements View.OnClickListener{

    private Boolean alreadyClicked=false;
    LocationRequest  locationRequest;
    FusedLocationProviderClient fusedLocationClient;
    public static final String MY_PREFS_FILENAME= "com.example.womensaftey.myfile";


    private Context context;
    private TextView alrttxt;
    private ImageView alrtimg;
    private Button btncncl;
    View view;


    Home_frag(Context context){
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.homefrag_layout,container,false);
        alrtimg=view.findViewById(R.id.alrtimg);
        alrttxt=view.findViewById(R.id.alrttxtx);
        btncncl=view.findViewById(R.id.btncncl);
        alrtimg.setOnClickListener(this);
        btncncl.setOnClickListener(this);
        return view;
    }



    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences preferences=context.getSharedPreferences(MY_PREFS_FILENAME,Context.MODE_PRIVATE);
        alreadyClicked=preferences.getBoolean("check",false);

        if(alreadyClicked==true){
            alrttxt.setVisibility(View.VISIBLE);
            btncncl.setEnabled(true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        SharedPreferences.Editor editor=context.
                getSharedPreferences(MY_PREFS_FILENAME,Context.MODE_PRIVATE).edit();

        editor.putBoolean("check",alreadyClicked);
        editor.commit();
    }

    public void sendLocationUpdates(){
        fusedLocationClient= LocationServices.getFusedLocationProviderClient(context);
        createLocationRequest();

        fusedLocationClient.requestLocationUpdates(locationRequest,new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Log.d(TAG, "onLocationResult: loc Changed");

                LocationUpdateTask locationUpdateTask=new LocationUpdateTask(locationResult);
                locationUpdateTask.execute();

            }
        }, Looper.getMainLooper());
    }

    public void sendLinkTOguardians(){


    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.alrtimg:
                if(alreadyClicked==false){
                    alreadyClicked=true;
                    alrttxt.setVisibility(View.VISIBLE);

                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("message");
                    mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("canCheck").setValue(true);

                    sendLocationUpdates();
                    sendLinkTOguardians();
                }
                break;

            case R.id.btncncl:
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("message");
                mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("canCheck").setValue(false);
                btncncl.setEnabled(true);
                alreadyClicked=false;
                alrttxt.setVisibility(View.INVISIBLE);
                break;
        }
    }

    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    public class LocationUpdateTask extends AsyncTask<Void,Void,Void> {
        LocationResult locationResult;

        public LocationUpdateTask(LocationResult locationResult){
            this.locationResult=locationResult;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            LatLng latLng=new LatLng(locationResult.getLastLocation().getLatitude(),locationResult.getLastLocation().getLongitude());
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("message");
            mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("latLng").setValue(latLng);
            return null;
        }
    }


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


