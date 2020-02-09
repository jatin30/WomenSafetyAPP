package com.example.womensaftey;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

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

    final String[] contactNo = new String[3];
    String msg;

    public void sendLinkTOguardians() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, 0);
        } else {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            final String uid = user.getUid();

            final String[] username = new String[1];


            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference mref = database.getReference("message");
            mref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    username[0] = dataSnapshot.child("Username").getValue(String.class);
                    contactNo[0] = dataSnapshot.child("gurdianContact1").getValue(String.class);
                    contactNo[1] = dataSnapshot.child("gurdianContact2").getValue(String.class);
                    contactNo[2] = dataSnapshot.child("gurdianContact3").getValue(String.class);

                    String url = "https://womensafetyapp.com/Required+Help+this+is+an+emergency/" + uid;

                    msg = "Hey this is " + username[0] + ", I am in trouble Please help me!"
                            + "I am using Women Safety app, " +
                            "You get a link below Click on it if you are not a user, sign up " +
                            "and then again click on link you will get the location and direction  \n\n\n"
                            + url;


//                    SmsManager smsManager = SmsManager.getDefault();
//                    smsManager.sendTextMessage(contactNo[0], null, msg, null, null);
//                    smsManager.sendTextMessage(contactNo[1], null, msg, null, null);
//                    smsManager.sendTextMessage(contactNo[2], null, msg, null, null);

//                    sendSMS(contactNo[0], msg);
//                    sendSMS(contactNo[1], msg);
//                    sendSMS(contactNo[2], msg);


                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                        } else {
                            sendSMSinLollipop();
                        }
                    } else {
                        sendSMS(contactNo[0], msg);
                        sendSMS(contactNo[1], msg);
                        sendSMS(contactNo[2], msg);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

    }


    private void sendSMSinLollipop() {

        SharedPreferences preferences = context.getSharedPreferences(MY_PREFS_FILENAME, Context.MODE_PRIVATE);
        int slot = preferences.getInt("slot", 0);

        SubscriptionManager subscriptionManager;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            subscriptionManager = SubscriptionManager.from(context);
            List<SubscriptionInfo> subscriptionInfoList;

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            }
            subscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
            int no=subscriptionInfoList.size();
            int subscriptionId = 0;
            if(no>1){
                if (slot == 0) {
                    subscriptionId = subscriptionInfoList.get(0).getSubscriptionId();
                } else if (slot == 1) {
                    subscriptionId = subscriptionInfoList.get(1).getSubscriptionId();
                }
            }
            else{
                subscriptionId=subscriptionInfoList.get(0).getSubscriptionId();
            }
            sendSMS(contactNo[0], msg, subscriptionId);
            sendSMS(contactNo[1], msg, subscriptionId);
            sendSMS(contactNo[2], msg, subscriptionId);
        }


    }

    private void sendSMS(String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        final Context baseContext = context;
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(baseContext, "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(baseContext, "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(baseContext, "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(baseContext, "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(baseContext, "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(baseContext, "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(baseContext, "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void sendSMS(String phoneNumber, String message, int subscriptionId) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        final Context baseContext = context;
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(baseContext, "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(baseContext, "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(baseContext, "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(baseContext, "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(baseContext, "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(baseContext, "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(baseContext, "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getSmsManagerForSubscriptionId(subscriptionId);
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "Thanks! Permission Granted", Toast.LENGTH_SHORT).show();
                    sendLinkTOguardians();
                } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.SEND_SMS)) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        dialog.setMessage("This Permission is important, Please permit it!")
                                .setTitle("Important permission Denied!");

                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, 0);
                            }
                        });
                    }
                } else {
                    Toast.makeText(context, "We will never show this to you again", Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "Thanks! Permission Granted", Toast.LENGTH_SHORT).show();
                    sendSMSinLollipop();
                } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_PHONE_STATE)) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        dialog.setMessage("This Permission is important, Please permit it!")
                                .setTitle("Important permission Denied!");

                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                            }
                        });
                    }
                } else {
                    Toast.makeText(context, "We will never show this to you again", Toast.LENGTH_SHORT).show();
                }
                break;

        }
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


