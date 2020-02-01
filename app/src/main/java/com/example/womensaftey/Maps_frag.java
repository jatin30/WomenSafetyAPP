package com.example.womensaftey;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class Maps_frag extends Fragment implements OnMapReadyCallback {

    SupportMapFragment supportMapFragment;
    GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.maps_frag_layout, container, false);
        supportMapFragment=(SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if(supportMapFragment==null){
            FragmentManager fm=getFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            supportMapFragment=SupportMapFragment.newInstance();
            ft.replace(R.id.map,supportMapFragment).commit();
        }
        supportMapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        final LatLng[] latLng = new LatLng[1];
//        fusedLocationClient.getLastLocation()
//                .addOnSuccessListener( getActivity(), new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        // Got last known location. In some rare situations this can be null.
//                        if (location != null) {
//                            latLng[0] =new LatLng(location.getLatitude(),location.getLongitude());
//                        }
//                        else{
//                            latLng[0] =new LatLng(0,0);
//                        }
//                    }
//                });

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        latLng[0] =new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(latLng[0]).title("Your Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng[0]));
    }
}
