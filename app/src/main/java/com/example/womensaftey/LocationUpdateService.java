package com.example.womensaftey;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

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

public class LocationUpdateService extends Service {

    private Context context=LocationUpdateService.this;
    private NotificationManager manager;
    private String CHANNEL_ID="LocationUpdates";
    private String CHANNEL_NAME="Location Updates";

    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationClient;
    LocationUpdateTask locationUpdateTask;


//    public LocationUpdateService(){
//        super("Hello");
//
//    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        //createNotificationChannel(task,desc);

//        Notification notification = new NotificationCompat.Builder(context, channelId)
//                .setContentTitle(task)
//                .setContentText(desc)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .build();

        locationUpdateTask= new LocationUpdateTask(this);
        locationUpdateTask.execute();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fusedLocationClient.removeLocationUpdates(locationCallback);
        locationUpdateTask.cancel(true);
    }



    public LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Log.d(TAG, "onLocationResult: loc Changed");

//            LocationUpdateTask locationUpdateTask = new LocationUpdateTask(locationResult);
//            locationUpdateTask.execute();

            LatLng latLng = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());


            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("message");
            mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("latLng").setValue(latLng);

            Toast.makeText(context, "Location Changed on Database", Toast.LENGTH_SHORT).show();
        }
    };


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


    public class LocationUpdateTask extends AsyncTask<Void, Void, Void> {
        Context context;

        public LocationUpdateTask(Context context) {
            this.context=context;
        }


        @Override
        protected Void doInBackground(Void... voids) {

            String task="Location Updates";
            String desc="Your Location is sending to your guardians, Tap to disable it";

            Intent intent1=new Intent(context,MainActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent=PendingIntent.getActivity(context,2,intent1,0);

            NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(context,CHANNEL_ID)
                    .setContentTitle(task)
                    .setContentText(desc)
                    .setSmallIcon(R.drawable.wmnsftyicon)
                    .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            sendLocationUpdates();
            startForeground(101,notificationBuilder.build());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }



    //    private void createNotificationChannel(String task, String desc) {
//
//        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new
//                    NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
//            //manager = getSystemService(NotificationManager.class);
//
//            //(NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//
//            manager.createNotificationChannel(channel);
//        }
//
//    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



}
