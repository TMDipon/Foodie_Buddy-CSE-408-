package com.example.foodie_buddy;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class userProfile extends AppCompatActivity {

    public TabLayout tabs;

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }


    public String getAddress(String a, String b, String c, String d, String e, String f) {
        if (e.equalsIgnoreCase("null")) {
            e = "";
        } else {
            e += ", ";
        }
        if (c.equalsIgnoreCase("null")) {
            c = "";
        } else {
            c += ", ";
        }

        return f + ", " + e + d + ", " + c + b + ", " + a;
    }

    public ArrayList<String> restaurants;

    public LocationManager locationManager;

    public LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        if (!sharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            Intent i = new Intent("com.example.foodie_buddy.loginActivity");
            startActivity(i);
        }

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);//It will allow us to access the location service on the phone

        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                //Log.i("Location", location.toString());
                sharedPrefManager.getInstance(getApplicationContext()).saveLocation(location.getLatitude(), location.getLongitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {}

            @Override
            public void onProviderEnabled(String s) {}

            @Override
            public void onProviderDisabled(String s) {}

        };

        if (Build.VERSION.SDK_INT < 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        }
        else
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                // ask for permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            else
            {
                // we have permission!
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }

        }

        //Here starts the code for ui of the initial page of user profile
        tabs = (TabLayout)findViewById(R.id.typetab);
        final ArrayList<String> types = new ArrayList<String>();


        StringRequest s = new StringRequest(Request.Method.GET, constants.showres_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                sharedPrefManager.getInstance(getApplicationContext()).saveRestaurants(response);
                try
                {
                    JSONArray J =new JSONArray(sharedPrefManager.getInstance(getApplicationContext()).getRestaurants());
                    for(int i=0;i<J.length();i++)
                    {
                        JSONObject k = J.getJSONObject(i);
                        if(!types.contains(k.getString("type")))
                        {
                            types.add(k.getString("type"));
                        }
                    }
                }
                catch(JSONException x)
                {}

                for(int i=0;i<types.size();i++)
                {
                    tabs.addTab(tabs.newTab().setText(types.get(i)));
                }
                tabs.setTabGravity(TabLayout.GRAVITY_FILL);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        RequestHandler.getInstance(this).addToRequestQueue(s);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                sharedPrefManager.getInstance(getApplicationContext()).saveClickedRestaurantType((String) tab.getText());

                final ListView lv = (ListView)findViewById(R.id.foodList);
                final ArrayList<Restaurants> rests = new ArrayList<Restaurants>();

                try
                {
                    JSONArray J =new JSONArray(sharedPrefManager.getInstance(getApplicationContext()).getRestaurants());
                    String t = sharedPrefManager.getInstance(getApplicationContext()).getClickedType();

                    Location first = new Location("");
                    first.setLatitude(sharedPrefManager.getInstance(getApplicationContext()).getLatitude());
                    first.setLongitude(sharedPrefManager.getInstance(getApplicationContext()).getLongitude());

                    for(int i=0;i<J.length();i++)
                    {
                        JSONObject k = J.getJSONObject(i);
                        String address = getAddress(k.getString("district"),k.getString("area"),k.getString("Road_name"),k.getString("Road_no"),k.getString("House_name"),k.getString("House_no"));
                        LatLng ll = getLocationFromAddress(getApplicationContext(),address);
                        if(ll == null)
                        {
                            Toast.makeText(userProfile.this, address, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            if(k.getString("type").equals(t))
                            {
                                Location second = new Location("");
                                second.setLatitude(ll.latitude);
                                second.setLongitude(ll.longitude);

                                double distanceInMeters = first.distanceTo(second)/1000.0;
                                //Toast.makeText(userProfile.this,k.getString("name")+" "+Double.toString(distanceInMeters), Toast.LENGTH_SHORT).show();
                                if(distanceInMeters <= 3.00000)
                                {
                                    Restaurants r = new Restaurants(k.getInt("id"),k.getString("name"),k.getString("type"),k.getString("starts_at"),k.getString("closes_at"));
                                    rests.add(r);
                                }
                            }
                        }
                    }
                }
                catch(JSONException x){}

                restaurantAdapter adapter = new restaurantAdapter(rests, getApplicationContext());
                lv.setAdapter(adapter);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

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
                sharedPrefManager.getInstance(this).logout();
                finish();
                startActivity(new Intent(this,loginActivity.class));
                break;
        }
        return true;
    }
}
