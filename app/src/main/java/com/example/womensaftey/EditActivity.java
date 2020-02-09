package com.example.womensaftey;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditActivity extends AppCompatActivity {


    private EditText gurdname, gurdnumber;
    private String gurdianName;
    private String gurdianContact;
    private Button updatebtn;
    private int guardno;

    private Toolbar toolbar;
    private SwipeRefreshLayout swip;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_guardian);


        Bundle bundle=getIntent().getExtras();
        guardno=bundle.getInt("Guard");

        initviews();


        swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swip.setRefreshing(false);
            }
        });

        //mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swip.setColorSchemeColors(Color.BLUE, Color.YELLOW, Color.RED,Color.GREEN);
                swip.setRefreshing(true);
                gurdianName = gurdname.getText().toString().trim();
                gurdianContact = gurdnumber.getText().toString().trim();
                switch (guardno) {
                    case 1:
                        update(1);
                      break;
                    case 2:
                        update(2);
                      break;
                    case 3:
                        update(3);
                        break;


                }
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    void update(int no)
    {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("message");
        mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("gurdianName"+no).setValue(gurdianName);
        mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("gurdianContact"+no).setValue(gurdianContact);
        Intent intent=new Intent(EditActivity.this,ProfileActivity.class);
        startActivity(intent);
        finish();
        swip.setRefreshing(false);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    void initviews() {
        gurdname = findViewById(R.id.guardname);
        gurdnumber = findViewById(R.id.guardnumber);
        updatebtn=findViewById(R.id.confirm);
        toolbar = findViewById(R.id.toolbar);
        swip=findViewById(R.id.swip1);
    }
}

