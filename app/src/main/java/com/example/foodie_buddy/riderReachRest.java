package com.example.foodie_buddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class riderReachRest extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback{

    private GoogleMap mMap;
    private MarkerOptions place1, place2;
    private Polyline currentPolyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_reach_rest);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapridtores);
        mapFragment.getMapAsync(this);

        double rlat = sharedRiderManager.getInstance(getApplicationContext()).getriLatitude();
        double rlon = sharedRiderManager.getInstance(getApplicationContext()).getriLongitude();
        double reslat = sharedRiderManager.getInstance(getApplicationContext()).getrestLatitude();
        double reslon = sharedRiderManager.getInstance(getApplicationContext()).getrestLongitude();
        Log.i("Locations: ",Double.toString(rlat)+" "+Double.toString(rlon)+" "+Double.toString(reslat)+" "+Double.toString(reslon));

        place1 = new MarkerOptions().position(new LatLng(rlat, rlon)).title("Your place");
        place2 = new MarkerOptions().position(new LatLng(reslat, reslon)).title(sharedRiderManager.getInstance(getApplicationContext()).getRestName());

        new FetchURL(riderReachRest.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
    }


    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        String mode = "mode=" + directionMode;
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values)
    {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        mMap.addMarker(place1);
        mMap.addMarker(place2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.menulogout:
                sharedRiderManager.getInstance(this).riderlogout();
                finishAffinity();
                startActivity(new Intent(this,riderLogin.class));
                break;
        }
        return true;
    }
}
