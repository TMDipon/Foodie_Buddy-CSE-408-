package com.example.foodie_buddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
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

public class riderOrderDetail extends AppCompatActivity {

    private TextView a,b,c;
    private ListView x;
    private ArrayList<String> l1;
    private CountDownTimer mCountDownTimer;

    @Override
    public void onBackPressed() {
            new AlertDialog.Builder(this)
                    .setTitle("Exit?")
                    .setMessage("Are you sure you want to go back?\nThis order will be lost")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            mCountDownTimer.cancel();
                            Toast.makeText(getApplicationContext(), "Order Cancelled", Toast.LENGTH_SHORT).show();

                            StringRequest s = new StringRequest(Request.Method.POST, constants.rejectOrder_URL, new Response.Listener<String>() {
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
                                    return m;
                                }
                            };

                            RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(s);
                            sharedRiderManager.getInstance(getApplicationContext()).setOnlineStatus(0);
                            finish();
                            startActivity(new Intent(getApplicationContext(),riderProfile.class));
                        }
                    }).create().show();
    }

    public void accept(View v)
    {
        mCountDownTimer.cancel();
        Toast.makeText(getApplicationContext(), "You accepted the order", Toast.LENGTH_SHORT).show();

        StringRequest s = new StringRequest(Request.Method.POST, constants.acceptOrder_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                //finish();
                //startActivity(new Intent(getApplicationContext(),riderProfile.class));
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
                m.put("rid", Integer.toString(sharedRiderManager.getInstance(getApplicationContext()).getRiderId()));
                return m;
            }
        };

        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(s);

    }


    public void reject(View v)
    {
        mCountDownTimer.cancel();
        Toast.makeText(getApplicationContext(), "Order Cancelled as you rejected it\nTaking tou to Offline mood", Toast.LENGTH_SHORT).show();

        StringRequest s = new StringRequest(Request.Method.POST, constants.rejectOrder_URL, new Response.Listener<String>() {
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
                return m;
            }
        };

        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(s);

        sharedRiderManager.getInstance(getApplicationContext()).setOnlineStatus(0);
        finish();
        startActivity(new Intent(getApplicationContext(),riderProfile.class));

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_order_detail);

        mCountDownTimer = new CountDownTimer(80000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.i("Time left:", Long.toString(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                Toast.makeText(getApplicationContext(), "Order Cancelled as you didn't  respond within time", Toast.LENGTH_SHORT).show();

                StringRequest s = new StringRequest(Request.Method.POST, constants.rejectOrder_URL, new Response.Listener<String>() {
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
                        return m;
                    }
                };

                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(s);

                sharedRiderManager.getInstance(getApplicationContext()).setOnlineStatus(0);
                finish();
                startActivity(new Intent(getApplicationContext(),riderProfile.class));
            }
        }.start();

        a = (TextView)findViewById(R.id.odrfromRest);
        b = (TextView)findViewById(R.id.restaddr2);
        c = (TextView)findViewById(R.id.odrtotval);
        x = (ListView)findViewById(R.id.odritems);

        a.setText("Order from: " + sharedRiderManager.getInstance(getApplicationContext()).getRestName());
        b.setText(sharedRiderManager.getInstance(getApplicationContext()).getRestAddress());

        l1 = sharedRiderManager.getInstance(getApplicationContext()).getOrderItems();
        ArrayAdapter a1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, l1);
        x.setAdapter(a1);

        double subtotal = sharedRiderManager.getInstance(getApplicationContext()).getTotalPrice();
        double tax = subtotal * 0.15;
        double dfee = 60.0;

        double total = subtotal + tax + dfee;
        c.setText(Double.toString(total));

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
