package com.example.foodie_buddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ownerProfile extends AppCompatActivity {

    public void addRestaurant(View v)
    {
        Intent i = new Intent("com.example.foodie_buddy.createRestaurant");
        startActivity(i);
    }

    public void changeRestaurant(View v)
    {
        StringRequest s4 = new StringRequest(Request.Method.POST, constants.mrestowner_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                sharedOwnerManager.getInstance(getApplicationContext()).saveRestaurantsUnder(response);
                startActivity(new Intent(getApplicationContext(),modifiableRestaurants.class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage()+"haha", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> m = new HashMap<>();
                m.put("id", Integer.toString(sharedOwnerManager.getInstance(getApplicationContext()).getOwnerId()));
                return m;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(s4);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_profile);

        if(!sharedOwnerManager.getInstance(this).isOwnerLoggedIn())
        {
            finish();
            startActivity(new Intent(this, ownerLogin.class));
        }
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
                sharedOwnerManager.getInstance(this).logout();
                finish();
                startActivity(new Intent(this,ownerLogin.class));
                break;
        }
        return true;
    }
}