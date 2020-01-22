package com.example.womensaftey;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private FirebaseAuth mAuth;
    private BottomNavigationView bottomNavigationView;

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

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Home_frag frag = new Home_frag();
        fragmentTransaction.replace(R.id.fragment_container, frag);
        fragmentTransaction.commit();

        bottomNavigationView.setSelectedItemId(R.id.homes);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.homes:
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        Home_frag frag = new Home_frag();
                        fragmentTransaction.replace(R.id.fragment_container, frag);
                        fragmentTransaction.commit();
                        break;
                    case R.id.maps:
                        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                        Maps_frag frag1 = new Maps_frag();
                        fragmentTransaction1.replace(R.id.fragment_container, frag1);
                        fragmentTransaction1.commit();
                        break;
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
                finish();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
        }
        return false;
    }

    public void initviews() {
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation_drawer);
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottom_navigationPanel);
    }

}
