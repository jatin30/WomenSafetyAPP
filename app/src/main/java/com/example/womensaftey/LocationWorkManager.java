package com.example.womensaftey;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

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

public class LocationWorkManager extends Worker {



    private Context context;
    private WorkerParameters parameters;

    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationClient;
    LocationCallback locationCallback;


    public LocationWorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context=context;
        parameters=workerParams;
    }

    @NonNull
    @Override
    public Result doWork() {

        Log.d(TAG, "doWork: called");

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Log.d(TAG, "onLocationResult: loc Changed");

//                LocationUpdateTask locationUpdateTask = new LocationUpdateTask(locationResult);
//                locationUpdateTask.execute();

                LatLng latLng = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());


                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("message");
                mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("latLng").setValue(latLng);
            }
        };

        sendLocationUpdates();

//        for(int i=0;i<50;i++){
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            Throwable tr = null;
//            Log.e(TAG, "doWork: number: "+i,tr );
//        }

        return Result.success();
    }


    @Override
    public void onStopped() {
        super.onStopped();
        Toast.makeText(context, "LocationWorkManager OnStopped Called!!", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onStopped: locationWorkManager Called!");
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }




    public void sendLocationUpdates() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        createLocationRequest();

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }


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
//
//
//            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("message");
//            mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("latLng").setValue(latLng);
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            Toast.makeText(context, "Location Changed on Database", Toast.LENGTH_SHORT).show();
//        }
//    }
}
