package com.example.womensaftey;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private TextView profuser, profemail, gurd1name, gurd1no, gurd2name, gurd2no, gurd3name, gurd3no;
    private TextView edit1, edit2, edit3;
    private LinearLayout linlayprof;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initview();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        profemail.setText(email);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        String userid = user.getUid();
        edit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditActivity.class);
                intent.putExtra("Guard", 1);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });
        edit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditActivity.class);
                intent.putExtra("Guard", 2);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });
        edit3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditActivity.class);
                intent.putExtra("Guard", 3);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });
        myRef.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User user1 =dataSnapshot.getValue(User.class);
                setview(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void setview(DataSnapshot dataSnapshot) {
        profuser.setText(dataSnapshot.child("Username").getValue(String.class));
        gurd1name.setText(dataSnapshot.child("gurdianName1").getValue(String.class));
        gurd1no.setText(dataSnapshot.child("gurdianContact1").getValue(String.class));
        gurd2name.setText(dataSnapshot.child("gurdianName2").getValue(String.class));
        gurd2no.setText(dataSnapshot.child("gurdianContact2").getValue(String.class));
        gurd3name.setText(dataSnapshot.child("gurdianName3").getValue(String.class));
        gurd3no.setText(dataSnapshot.child("gurdianContact3").getValue(String.class));

        linlayprof.setVisibility(View.VISIBLE);
    }

    void initview() {
        profuser = findViewById(R.id.profusername);
        profemail = findViewById(R.id.profemail);
        gurd1name = findViewById(R.id.gurd1name);
        gurd1no = findViewById(R.id.gurd1no);
        gurd2name = findViewById(R.id.gurd2name);
        gurd2no = findViewById(R.id.gurd2no);
        gurd3name = findViewById(R.id.gurd3name);
        gurd3no = findViewById(R.id.gurd3no);
        edit1 = findViewById(R.id.edit1);
        edit2 = findViewById(R.id.edit2);
        edit3 = findViewById(R.id.edit3);
        linlayprof=findViewById(R.id.linlayprof);
        toolbar = findViewById(R.id.toolbar);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
