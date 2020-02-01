package com.example.womensaftey;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText lgneml,lgnpaswd;
    private Button lgnbtn;
    private TextView sgntxt;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        initViews();
        request();
        Log.e("MyTAgs","inside if");
        mAuth = FirebaseAuth.getInstance();
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser=mAuth.getCurrentUser();
                if(firebaseUser!=null){
                    Toast.makeText(LoginActivity.this, "You are logged In Already!", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        };


        lgnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.YELLOW, Color.BLUE);
                swipeRefreshLayout.setRefreshing(true);
                if(validate()){
                    String email = lgneml.getText().toString().trim();
                    String password = lgnpaswd.getText().toString().trim();
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                swipeRefreshLayout.setRefreshing(false);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();

                            } else {
                                swipeRefreshLayout.setRefreshing(false);
                                Toast.makeText(LoginActivity.this, "There is no such user!", Toast.LENGTH_SHORT).show();
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG);
                            }
                        }
                    });
                }
                else{
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(LoginActivity.this, "Validate not successful", Toast.LENGTH_SHORT).show();
                }


            }
        });

        sgntxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,SignUp_Activity.class);
                startActivity(intent);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }





    @Override
    protected void onStart() {
        super.onStart();
//        mAuth.addAuthStateListener(authStateListener);
    }

    public boolean validate() {
        boolean valid = true;

        String email0 = lgneml.getText().toString();
        String password = lgnpaswd.getText().toString();


        if (email0.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email0).matches()) {
            lgneml.setError("enter a valid email address");
            valid = false;
        } else {
            lgneml.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 10) {
            lgnpaswd.setError("between 6 and 10 alphanumeric characters");
            valid = false;
        } else {
            lgnpaswd.setError(null);
        }

        return valid;
    }

    public void request()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},0);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},2);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case 0:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Thanks! Permission Granted", Toast.LENGTH_SHORT).show();
                }
                else if(grantResults[0]==PackageManager.PERMISSION_DENIED){
                    if(ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,Manifest.permission.SEND_SMS)){
                        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
                        dialog.setMessage("This Permission is important, Please permit it!")
                                .setTitle("Important permission Denied!");

                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(LoginActivity.this,new String[]{Manifest.permission.SEND_SMS},0);
                            }
                        });
                    }
                }
                else{
                    Toast.makeText(this, "We will never show this to you again", Toast.LENGTH_SHORT).show();
                }

            case 1:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Thanks! Permission Granted", Toast.LENGTH_SHORT).show();
                }
                else if(grantResults[0]==PackageManager.PERMISSION_DENIED){
                    if(ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)){
                        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
                        dialog.setMessage("This Permission is important, Please permit it!")
                                .setTitle("Important permission Denied!");

                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(LoginActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                            }
                        });
                    }
                }
                else{
                    Toast.makeText(this, "We will never show this to you again", Toast.LENGTH_SHORT).show();
                }

            case 2:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Thanks! Permission Granted", Toast.LENGTH_SHORT).show();
                }
                else if(grantResults[0]==PackageManager.PERMISSION_DENIED){
                    if(ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,Manifest.permission.READ_PHONE_STATE)){
                        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
                        dialog.setMessage("This Permission is important, Please permit it!")
                                .setTitle("Important permission Denied!");

                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(LoginActivity.this,new String[]{Manifest.permission.READ_PHONE_STATE},2);
                            }
                        });
                    }
                }
                else{
                    //We can show here snakebar also
                    Toast.makeText(this, "We will never show this to you again", Toast.LENGTH_SHORT).show();
                }

        }
    }

    public void initViews(){
        lgnbtn=findViewById(R.id.lgnbtn);
        lgneml=findViewById(R.id.lgneml);
        lgnpaswd=findViewById(R.id.lgnpaswd);
        sgntxt=findViewById(R.id.sgntxt);
        swipeRefreshLayout=findViewById(R.id.swip);
    }
}
