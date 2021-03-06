package com.example.foodie_buddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class riderReceiveOrder extends AppCompatActivity {

    private oitemadapter oad;
    private ListView lv;
    private TextView tv;

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please receive the order\nThen reach the delivery location", Toast.LENGTH_LONG).show();
    }

    public void allItemsTaken(View v)
    {
        if(oad.allChecked())
        {
            //start new activity to reach the delivery location
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
                    m.put("order_stat","Order Received from Restaurant, On the way");
                    return m;
                }
            };

            RequestHandler.getInstance(this).addToRequestQueue(s);
            finish();
            startActivity(new Intent(getApplicationContext(),riderReachUser.class));
        }
        else
        {
            Toast.makeText(this, "Please check the items to be taken\nThen proceed to the next task", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_receive_order);

        lv = (ListView)findViewById(R.id.odritems2);
        oad = new oitemadapter(sharedRiderManager.getInstance(getApplicationContext()).getOrderItems(),getApplicationContext(),0);
        lv.setAdapter(oad);

        tv = (TextView)findViewById(R.id.odrpayval);

        double subtotal = sharedRiderManager.getInstance(getApplicationContext()).getTotalPrice();
        double vat = subtotal * 0.15;

        double total = subtotal + vat;
        tv.setText("BDT: "+Double.toString(Math.ceil(total)));
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
