package com.example.womensaftey;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SettingActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button btnsim;

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        toolbar=findViewById(R.id.toolbar);
        btnsim=findViewById(R.id.btnsim);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnsim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimDialog simDialog=new SimDialog(SettingActivity.this);
                simDialog.show(getSupportFragmentManager(),"SimDialog");
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
