package com.example.foodie_buddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class riderConfirmRest extends AppCompatActivity {
    private ListView x;
    private ArrayList<String> l1;

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please complete all of your tasks first to deliver this order", Toast.LENGTH_SHORT).show();
    }

    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }

    public void makeCall(View v)
    {
        dialContactPhone(sharedRiderManager.getInstance(getApplicationContext()).getRestPhone());
    }

    public void confirmed(View v)
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
                m.put("order_stat","Order Confirmed with Restaurant");
                return m;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(s);
        finish();
        startActivity(new Intent(getApplicationContext(),riderReachRest.class));
        Toast.makeText(this, "Order confirmed with restaurant", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_confirm_rest);

        x = (ListView)findViewById(R.id.odritems1);
        l1 = sharedRiderManager.getInstance(getApplicationContext()).getOrderItems();
        ArrayAdapter a1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, l1);
        x.setAdapter(a1);
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
