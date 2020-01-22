package com.example.womensaftey;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class SplashScreenActivity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth mAuth;
    private boolean Flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);
        mAuth = FirebaseAuth.getInstance();

        LogoLauncher logoLauncher=new LogoLauncher(authStateListener,mAuth,this);
        logoLauncher.run();

        if(Flag){
            Intent intent=new Intent(SplashScreenActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            SplashScreenActivity.this.startActivity(intent);
        }
        else{
            Intent intent=new Intent(SplashScreenActivity.this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            SplashScreenActivity.this.startActivity(intent);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }
}



class AsyncTask extends android.os.AsyncTask<Void,Void,Void>{

    @Override
    protected Void doInBackground(Void... voids) {

        return null;
    }
}