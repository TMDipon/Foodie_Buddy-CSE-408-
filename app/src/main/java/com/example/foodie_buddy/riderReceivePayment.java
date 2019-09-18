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

public class riderReceivePayment extends AppCompatActivity {

    private oitemadapter oad;
    private ListView lv;
    private TextView tv;

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please receive your payment first\nThen you can look for another order if you want", Toast.LENGTH_LONG).show();
    }

    public void paymentTaken(View v)
    {
            //start new activity to look for further orders
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
                    m.put("order_stat","Order Delivered, Task Complete");
                    return m;
                }
            };

            RequestHandler.getInstance(this).addToRequestQueue(s);
            finish();
            startActivity(new Intent(getApplicationContext(),riderProfile.class));
            Toast.makeText(this, "Order deliver completed", Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_receive_payment);

        lv = (ListView)findViewById(R.id.odritems3);
        oad = new oitemadapter(sharedRiderManager.getInstance(getApplicationContext()).getOrderItems(),getApplicationContext(),1);
        lv.setAdapter(oad);

        tv = (TextView)findViewById(R.id.odrpayval2);

        double subtotal = sharedRiderManager.getInstance(getApplicationContext()).getTotalPrice();
        double vat = subtotal * 0.15;
        double dfee = 60.0;

        double total = subtotal + vat + dfee;
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
