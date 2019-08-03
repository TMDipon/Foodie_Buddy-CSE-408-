package com.example.foodie_buddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class userProfile extends AppCompatActivity {

    public void showFoods()
    {
        StringRequest s = new StringRequest(Request.Method.POST, constants.showfoods_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                sharedPrefManager.getInstance(getApplicationContext()).saveCurrentRestFoods(response);
                startActivity(new Intent(getApplicationContext(),restaurantFoods.class));
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
                m.put("name", sharedPrefManager.getInstance(getApplicationContext()).getCurrentRestaurant());
                return m;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(s);
    }

    public ArrayList<String> restaurants;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        if(!sharedPrefManager.getInstance(this).isLoggedIn())
        {
            finish();
            Intent i = new Intent("com.example.foodie_buddy.loginActivity");
            startActivity(i);
        }

        final TabLayout tb = (TabLayout)findViewById(R.id.typetab);
        final ArrayList<String> types = new ArrayList<String>();


        StringRequest s = new StringRequest(Request.Method.GET, constants.showres_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
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
                {

                }

                for(int i=0;i<types.size();i++)
                {
                    tb.addTab(tb.newTab().setText(types.get(i)));
                }
                tb.setTabGravity(TabLayout.GRAVITY_FILL);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) ;

        RequestHandler.getInstance(this).addToRequestQueue(s);

        tb.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                sharedPrefManager.getInstance(getApplicationContext()).saveClickedRestaurantType((String) tab.getText());

                final ListView lv = (ListView)findViewById(R.id.foodList);
                final ArrayList<Restaurants> rests = new ArrayList<Restaurants>();

                try
                {
                    JSONArray J =new JSONArray(sharedPrefManager.getInstance(getApplicationContext()).getRestaurants());
                    String t = sharedPrefManager.getInstance(getApplicationContext()).getClickedType();
                    for(int i=0;i<J.length();i++)
                    {
                        JSONObject k = J.getJSONObject(i);
                        if(k.getString("type").equals(t))
                        {
                            Restaurants r = new Restaurants(k.getInt("id"),k.getString("name"),k.getString("type"),k.getString("starts_at"),k.getString("closes_at"));
                            rests.add(r);
                        }
                    }
                }
                catch(JSONException x){}

                //tem[0] = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,restaurants);
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
