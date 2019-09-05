package com.example.findmyfriend;


import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;


import android.os.Bundle;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    ConstraintLayout drawer_laout;
    LinearLayout nav_laout;
    Animation animation_left_right,animation_right_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        drawer_laout = findViewById(R.id.drawer_layout);
        nav_laout = findViewById(R.id.nav);
        animation_left_right = AnimationUtils.loadAnimation(this, R.anim.lefttoright);
        animation_right_left = AnimationUtils.loadAnimation(this,R.anim.righttoleft);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
    public void onBackPressed() {

        nav_laout.setAnimation(animation_right_left);
        drawer_laout.setVisibility(View.GONE);
    }

    public void menu_open(View view){
        drawer_laout.setVisibility(View.VISIBLE);
        nav_laout.setAnimation(animation_left_right);
    }

    public void go_to_profile(View view){

    }
    public void go_to_massage(View view){

    }

}
