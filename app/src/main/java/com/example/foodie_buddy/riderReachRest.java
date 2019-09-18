package com.example.foodie_buddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.HashMap;
import java.util.Map;

public class riderReachRest extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback{

    private GoogleMap mMap;
    private MarkerOptions place1, place2;
    private Polyline currentPolyline;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please reach to the restaurant\nThen receive the order", Toast.LENGTH_LONG).show();
    }

    public void reached(View v)
    {
        Location first = new Location("");
        first.setLatitude(sharedRiderManager.getInstance(getApplicationContext()).getrcLatitude());
        first.setLongitude(sharedRiderManager.getInstance(getApplicationContext()).getrcLongitude());

        Location second = new Location("");
        second.setLatitude(sharedRiderManager.getInstance(getApplicationContext()).getrestLatitude());
        second.setLongitude(sharedRiderManager.getInstance(getApplicationContext()).getrestLongitude());

        Log.i("Location: ",first.toString()+" "+second.toString());
        double distance = first.distanceTo(second);
        //if(distance < 20.0000)
        if(1==1)
        {
            StringRequest s = new StringRequest(Request.Method.POST, constants.updateOrderStatus_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> m = new HashMap<>();
                    m.put("oid", sharedRiderManager.getInstance(getApplicationContext()).getOrderId());
                    m.put("order_stat","Reached Restaurant");
                    return m;
                }
            };

            RequestHandler.getInstance(this).addToRequestQueue(s);
            finish();
            startActivity(new Intent(getApplicationContext(),riderReceiveOrder.class));
        }
        else
        {
            Toast.makeText(this, "Yoy are not in the restaurant\n"+Double.toString(Math.round(distance))+" Meters away"+"\nReach there and then proceed to take the order", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_reach_rest);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);//It will allow us to access the location service on the phone

        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                sharedRiderManager.getInstance(getApplicationContext()).saveRiderCurrentLocation(location.getLatitude(), location.getLongitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {}

            @Override
            public void onProviderEnabled(String s) {}

            @Override
            public void onProviderDisabled(String s) {}

        };

        double lat = sharedRiderManager.getInstance(getApplicationContext()).getrcLatitude();
        double lon = sharedRiderManager.getInstance(getApplicationContext()).getrcLongitude();
        sharedRiderManager.getInstance(getApplicationContext()).saveRiderInitLocation(lat,lon);


        double rlat = sharedRiderManager.getInstance(getApplicationContext()).getriLatitude();
        double rlon = sharedRiderManager.getInstance(getApplicationContext()).getriLongitude();
        double reslat = sharedRiderManager.getInstance(getApplicationContext()).getrestLatitude();
        double reslon = sharedRiderManager.getInstance(getApplicationContext()).getrestLongitude();
        Log.i("Locations: ",Double.toString(rlat)+" "+Double.toString(rlon)+" "+Double.toString(reslat)+" "+Double.toString(reslon));

        place1 = new MarkerOptions().position(new LatLng(rlat, rlon)).title("Your place");
        place2 = new MarkerOptions().position(new LatLng(reslat, reslon)).title(sharedRiderManager.getInstance(getApplicationContext()).getRestName());

        new FetchURL(riderReachRest.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapridtores);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        mMap.addMarker(place1);
        mMap.addMarker(place2);
        CameraPosition googlePlex = CameraPosition.builder()
                .target(new LatLng(sharedRiderManager.getInstance(getApplicationContext()).getriLatitude(),sharedRiderManager.getInstance(getApplicationContext()).getriLongitude()))
                .zoom(15)
                .bearing(0)
                .tilt(45)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 5000, null);
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
