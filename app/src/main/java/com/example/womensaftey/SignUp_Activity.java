package com.example.womensaftey;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp_Activity extends AppCompatActivity {

    private static final String TAG = "SignUp_Activity";

    private EditText email, paswd, name, grdnam1, grdnam2, grdnam3, grdcont1, grdcont2, grdcont3;
    private Button signbtn,lgnbtn;
    private FirebaseAuth mAuth;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);

        initviews();
        mAuth = FirebaseAuth.getInstance();


        signbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
        lgnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });


    }


    public void signup() {
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.YELLOW, Color.RED,Color.GREEN);
        swipeRefreshLayout.setRefreshing(true);
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        signbtn.setEnabled(false);

        String email0 = email.getText().toString();
        String password = paswd.getText().toString();


        // TODO: Implement your own signup logic here.

        mAuth.createUserWithEmailAndPassword(email0, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    BackTask backTask=new BackTask();
                    backTask.execute();
                    onSignupSuccess();
                    Toast.makeText(getApplicationContext(), "You have registered successfully!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        swipeRefreshLayout.setRefreshing(false);
                        onSignupSuccess();
                        Toast.makeText(getApplicationContext(), "You have already registered !!", Toast.LENGTH_SHORT).show();

                    } else {
                        onSignupFailed();
                    }

                }
            }
        });

    }

    public class BackTask extends AsyncTask<Void,Void,Void>{

        private FusedLocationProviderClient fusedLocationClient;

        private String Username;
        private String gurdianName1;
        private String gurdianName2;
        private String gurdianName3;
        private String gurdianContact1;
        private String gurdianContact2;
        private String gurdianContact3;
        private LatLng latLng;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
             Username = name.getText().toString();
             gurdianName1 = grdnam1.getText().toString().trim();
             gurdianName2= grdnam2.getText().toString().trim();
             gurdianName3= grdnam3.getText().toString().trim();
             gurdianContact1= grdcont1.getText().toString().trim();
             gurdianContact2= grdcont2.getText().toString().trim();
             gurdianContact3= grdcont3.getText().toString().trim();


//            fusedLocationClient = LocationServices.getFusedLocationProviderClient(SignUp_Activity.this);
//            fusedLocationClient.getLastLocation()
//                    .addOnSuccessListener(SignUp_Activity.this, new OnSuccessListener<Location>() {
//                        @Override
//                        public void onSuccess(Location location) {
//                            // Got last known location. In some rare situations this can be null.
//                            if (location != null) {
//                                latLng=new LatLng(location.getLatitude(),location.getLongitude());
//                            }
//                            else{
//                                latLng=new LatLng(0,0);
//                            }
//                        }
//                    });

            latLng=new LatLng(0,0);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            User user = new User(Username,gurdianName1,
                    gurdianName2,gurdianName3,gurdianContact1,
                    gurdianContact2,gurdianContact3,latLng);

            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("message");
            mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);

            //mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }


    public void onSignupSuccess() {
        signbtn.setEnabled(true);
        swipeRefreshLayout.setRefreshing(false);
//        Intent intent = new Intent(SignUp_Activity.this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "SignUp Unsuccessful, Please Try Again!", Toast.LENGTH_LONG).show();
        signbtn.setEnabled(true);
        swipeRefreshLayout.setRefreshing(false);
    }

    public boolean validate() {
        boolean valid = true;

        String name0 = name.getText().toString();
        String email0 = email.getText().toString();
        String password = paswd.getText().toString();

        if (name0.isEmpty() || name0.length() < 3) {
            name.setError("at least 3 characters");
            valid = false;
        } else {
            name.setError(null);
        }

        if (email0.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email0).matches()) {
            email.setError("enter a valid email address");
            valid = false;
        } else {
            email.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 10) {
            paswd.setError("between 6 and 10 alphanumeric characters");
            valid = false;
        } else {
            paswd.setError(null);
        }

        return valid;
    }

    public void initviews() {
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        paswd = findViewById(R.id.paswd);
        signbtn = findViewById(R.id.sgnbtn);
        grdnam1 = findViewById(R.id.grdnam1);
        grdnam2 = findViewById(R.id.grdnam2);
        grdnam3 = findViewById(R.id.grdnam3);
        grdcont1 = findViewById(R.id.grdcont1);
        grdcont2 = findViewById(R.id.grdcont2);
        grdcont3 = findViewById(R.id.grdcont3);
        swipeRefreshLayout = findViewById(R.id.swip);
        lgnbtn=findViewById(R.id.lgnbck);
    }
}
