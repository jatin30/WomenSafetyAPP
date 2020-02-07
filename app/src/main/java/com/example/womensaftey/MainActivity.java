package com.example.womensaftey;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private TextView navuser,navemail;

    private FirebaseAuth mAuth;
    private BottomNavigationView bottomNavigationView;

    private int lastmenuItemID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initviews();
        mAuth = FirebaseAuth.getInstance();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_closed);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        navbarinit();

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
        if(appLinkData!=null){
            String Uid=appLinkData.getLastPathSegment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            Maps_frag frag = new Maps_frag(MainActivity.this,Uid);
            fragmentTransaction.replace(R.id.fragment_container, frag);
            fragmentTransaction.commit();
        }
        else{
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            Home_frag frag = new Home_frag(MainActivity.this);
            fragmentTransaction.replace(R.id.fragment_container, frag);
            fragmentTransaction.commit();
        }


        bottomNavigationView.setSelectedItemId(R.id.homes);
        lastmenuItemID = R.id.homes;
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId() == lastmenuItemID) {
                    return true;
                } else {
                    switch (menuItem.getItemId()) {

                        case R.id.homes:
                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            Home_frag frag = new Home_frag(MainActivity.this);
                            fragmentTransaction.replace(R.id.fragment_container, frag);
                            fragmentTransaction.commit();
                            lastmenuItemID = menuItem.getItemId();
                            break;
                        case R.id.maps:
                            FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                            Maps_frag frag1 = new Maps_frag(MainActivity.this);
                            fragmentTransaction1.replace(R.id.fragment_container, frag1);
                            fragmentTransaction1.commit();
                            lastmenuItemID = menuItem.getItemId();
                            break;
                    }
                }

                return true;
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.logout:
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();


        }
        return false;
    }

    private void navbarinit(){
        FirebaseAuth mauth= FirebaseAuth.getInstance();
        FirebaseUser user =mauth.getCurrentUser();
        String email=user.getEmail();
        navemail.setText(email);


        String userID =mauth.getCurrentUser().getUid();
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference mref=database.getReference("message");
        mref.child(userID).child("Username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Username = dataSnapshot.getValue(String.class);
                navuser.setText(Username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void initviews() {
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation_drawer);
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottom_navigationPanel);


        View headerView =navigationView.getHeaderView(0);
        navuser=headerView.findViewById(R.id.navuser);
        navemail=headerView.findViewById(R.id.navemail);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }
}
