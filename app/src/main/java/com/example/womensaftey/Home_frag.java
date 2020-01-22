package com.example.womensaftey;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Home_frag extends Fragment implements View.OnClickListener{

    private TextView alrttxt;
    private ImageView alrtimg;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.homefrag_layout,container,false);
        alrtimg=view.findViewById(R.id.alrtimg);
        alrttxt=view.findViewById(R.id.alrttxtx);
        alrtimg.setOnClickListener(this);
        return view;
    }

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

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.alrtimg:
                alrttxt.setVisibility(View.VISIBLE);
                break;
        }
    }
}
