package com.example.foodie_buddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class mrAddFood extends AppCompatActivity {

    public void mrAddFoodItem(View v)
    {
        startActivity(new Intent(getApplicationContext(),FoodInsert.class));
    }

    public void mrDeleteFoodItem(View v)
    {
        StringRequest s = new StringRequest(Request.Method.POST, constants.modfoodshow_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                sharedOwnerManager.getInstance(getApplicationContext()).saveCurRestFoods(response);
                startActivity(new Intent(getApplicationContext(),ownDeleteFood.class));
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
                m.put("id", Integer.toString(sharedOwnerManager.getInstance(getApplicationContext()).getCurrentResId()));
                return m;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(s);
    }

    public void changePrice(View v)
    {
        StringRequest s = new StringRequest(Request.Method.POST, constants.modfoodshow_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                sharedOwnerManager.getInstance(getApplicationContext()).saveCurRestFoods(response);
                startActivity(new Intent(getApplicationContext(),ownChangeFPrice.class));
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
                m.put("id", Integer.toString(sharedOwnerManager.getInstance(getApplicationContext()).getCurrentResId()));
                return m;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(s);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mr_add_food);
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
                finishAffinity();
                startActivity(new Intent(this,ownerLogin.class));
                break;
        }
        return true;
    }
}
