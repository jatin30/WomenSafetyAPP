package com.example.womensaftey;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Testing extends AppCompatActivity {


//    private FirebaseAuth mAuth;
//    private FirebaseAuth.AuthStateListener authStateListener;
//
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        setContentView(R.layout.activity_testing);
//
//        Handler handler=new Handler();
//        handler.postAtTime(new Runnable() {
//            @Override
//            public void run() {
//                authStateListener=new FirebaseAuth.AuthStateListener() {
//                    @Override
//                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                        FirebaseUser firebaseUser=mAuth.getCurrentUser();
//                        if(firebaseUser!=null){
//                            finish();
//                            Intent intent=new Intent(Testing.this,MainActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
//                        }
//                        else{
//                            Intent intent=new Intent(Testing.this,LoginActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
//                        }
//                    }
//                };
//            }
//        },3000);
//
//    }
//    @Override
//    protected void onStart() {
//        mAuth=FirebaseAuth.getInstance();
//        mAuth.addAuthStateListener(authStateListener);
//        super.onStart();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, Morld!");

    }



}
