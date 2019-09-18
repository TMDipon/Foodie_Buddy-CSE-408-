package com.example.foodie_buddy;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class userProfile extends AppCompatActivity implements Serializable {

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

    private LocationManager locationManager;

    private LocationListener locationListener;

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

    @TargetApi(Build.VERSION_CODES.O)
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

        sharedPrefManager.getInstance(getApplicationContext()).saveLocation(23.726665, 90.388138);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);//It will allow us to access the location service on the phone

        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                sharedPrefManager.getInstance(getApplicationContext()).saveLocation(location.getLatitude(), location.getLongitude());
                //Toast.makeText(userProfile.this, Double.toString(location.getLatitude()) +" "+Double.toString(location.getLongitude()), Toast.LENGTH_LONG).show();
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

        /*
        if(orderManager.getLength() > 0)
        {
            ArrayList<Order> orders = orderManager.getCurrentOrders();
            String temp = "Orders id: ";
            for(int i=0;i<orders.size();i++)
            {
                temp += Integer.toString(orders.get(i).getOrderId());
            }

            Toast.makeText(this, temp, Toast.LENGTH_LONG).show();
        }
        */

        int cstat = sharedPrefManager.getInstance(getApplicationContext()).getOrderCompletion();
        if(cstat == 2)//Order cancelled as no rider found
        {
            int orderId = sharedPrefManager.getInstance(getApplicationContext()).getOrderNo();
            int idx = orderManager.getOrderIndex(orderId);

            Order tem = orderManager.getOrder(idx);
            tem.cancel();
            String tem1 = new String("");
            tem1 = tem.getRestaurantName();

            //orderManager.Remove(idx);
            sharedPrefManager.getInstance(getApplicationContext()).saveOrderCompletetion(0);

            sharedPrefManager.getInstance(getApplicationContext()).saveOrderSource(1);


            Intent intent = new Intent(getApplicationContext(), orderBox.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            NotificationManager notificationManager = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "channel1";
                String description = "Created for notification";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel("1", name, importance);
                channel.setDescription(description);
                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(channel);
            }

            NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "1")
                    .setContentTitle("Order Cancelled")
                    .setContentText("Your order from "+tem1+" has been cancelled as no rider found\nTap to repeat this order")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("Your order from "+tem1+" has been cancelled as no rider found\nTap to repeat this order"))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(android.R.drawable.sym_def_app_icon)
                    .setAutoCancel(true);

            notificationManager.notify(0, notification.build());
        }
        else if(cstat == 1)//Order Completed
        {
            Intent in = new Intent(getApplicationContext(), foodConfirmed.class);
            startActivity(in);
        }
        else if(cstat == 3)//Order in progress
        {
            int orderId = sharedPrefManager.getInstance(getApplicationContext()).getOrderNo();
            int idx = orderManager.getOrderIndex(orderId);

            Order tem = orderManager.getOrder(idx);
            String tem1 = tem.getOrderStatus();

            NotificationManager notificationManager = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "channel1";
                String description = "Created for notification";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel("1", name, importance);
                channel.setDescription(description);
                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(channel);
            }

            NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "1")
                    .setContentTitle("Order progress")
                    .setContentText(tem1)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(tem1))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setSmallIcon(android.R.drawable.sym_def_app_icon)
                    .setAutoCancel(true);

            notificationManager.notify(0, notification.build());

            sharedPrefManager.getInstance(getApplicationContext()).saveOrderCompletetion(0);
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
                    Location second = new Location("");

                    for(int i=0;i<J.length();i++)
                    {
                        JSONObject k = J.getJSONObject(i);

                        if(k.getString("type").equals(t))
                        {
                            second.setLatitude(Double.parseDouble(k.getString("latitude")));
                            second.setLongitude(Double.parseDouble(k.getString("longitude")));

                            double distanceInMeters = first.distanceTo(second)/1000.0;
                            //Toast.makeText(userProfile.this,k.getString("name")+" "+Double.toString(distanceInMeters), Toast.LENGTH_SHORT).show();
                            if(distanceInMeters <= 4.500000)
                            {
                                Restaurants r = new Restaurants(k.getInt("id"),k.getString("name"),k.getString("type"),k.getString("starts_at"),k.getString("closes_at"),second.getLatitude(),second.getLongitude());
                                rests.add(r);
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
        getMenuInflater().inflate(R.menu.sidebar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.menulogout:
                sharedPrefManager.getInstance(this).logout();
                finishAffinity();
                startActivity(new Intent(this,loginActivity.class));
                break;

            case R.id.menuOrders:
                startActivity(new Intent(getApplicationContext(),currentOrders.class));
                break;
        }
        return true;
    }
}
