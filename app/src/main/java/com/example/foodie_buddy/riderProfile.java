package com.example.foodie_buddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class riderProfile extends AppCompatActivity {

    private TextView status;
    private Button changeStatus;
    private CountDownTimer mCountDownTimer;

    public void changeLookStatus(View v)
    {
        int condn = sharedRiderManager.getInstance(getApplicationContext()).getOnlineStat();
        status = (TextView)findViewById(R.id.rPStat);
        changeStatus = (Button)findViewById(R.id.changeRStat);

        if(condn == 0)
        {
            sharedRiderManager.getInstance(getApplicationContext()).setOnlineStatus(1);

            status.setText("Searching for Order.....");
            changeStatus.setText("Go Offline");
            changeStatus.setBackgroundColor(Color.parseColor("#00FF11"));

            mCountDownTimer = new CountDownTimer(20000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    Toast.makeText(getApplicationContext(), "No order found, Requesting again", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(),riderProfile.class));
                }
            }.start();

            StringRequest s = new StringRequest(Request.Method.POST, constants.searchOrder_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {
                    try {
                        JSONObject j = new JSONObject(response);
                        if(j.getBoolean("ostat"))
                        {
                            mCountDownTimer.cancel();
                            sharedRiderManager.getInstance(getApplicationContext()).saveOrder(response);
                            Toast.makeText(getApplicationContext(), "Order found in your prefferered area", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),riderOrderDetail.class));
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }

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
                    m.put("area", sharedRiderManager.getInstance(getApplicationContext()).getRiderPrefferedArea());
                    return m;
                }
            };

            RequestHandler.getInstance(this).addToRequestQueue(s);

        }
        else if(condn == 1)
        {
            sharedRiderManager.getInstance(getApplicationContext()).setOnlineStatus(0);

            status.setText("Currently Offline, Click below to look for orders");
            changeStatus.setText("Go Online");
            changeStatus.setBackgroundColor(Color.parseColor("#FF0000"));
            mCountDownTimer.cancel();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_profile);

        status = (TextView)findViewById(R.id.rPStat);
        changeStatus = (Button)findViewById(R.id.changeRStat);

        int condn = sharedRiderManager.getInstance(getApplicationContext()).getOnlineStat();
        if(condn == 0)
        {
            status.setText("Currently Offline, Click below to look for orders");
            changeStatus.setText("Go Online");
            changeStatus.setBackgroundColor(Color.parseColor("#FF0000"));
        }
        else if(condn == 1)
        {
            status.setText("Searching for Order.....");
            changeStatus.setText("Go Offline");
            changeStatus.setBackgroundColor(Color.parseColor("#00FF11"));

            mCountDownTimer = new CountDownTimer(20000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    Toast.makeText(getApplicationContext(), "Requesting again", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(),riderProfile.class));
                }
            }.start();

            StringRequest s = new StringRequest(Request.Method.POST, constants.searchOrder_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {
                    try {
                        JSONObject j = new JSONObject(response);
                        if(j.getBoolean("ostat"))
                        {
                            mCountDownTimer.cancel();
                            sharedRiderManager.getInstance(getApplicationContext()).saveOrder(response);
                            Toast.makeText(getApplicationContext(), "Order found in your prefferered area", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),riderOrderDetail.class));
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }

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
                    m.put("area", sharedRiderManager.getInstance(getApplicationContext()).getRiderPrefferedArea());
                    return m;
                }
            };

            RequestHandler.getInstance(this).addToRequestQueue(s);
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
                sharedRiderManager.getInstance(this).riderlogout();
                finish();
                startActivity(new Intent(this,riderLogin.class));
                break;
        }
        return true;
    }
}
