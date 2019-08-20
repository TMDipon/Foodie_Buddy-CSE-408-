package com.example.foodie_buddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class orderBox extends AppCompatActivity {

    public String getAddress(double lat,double lng) throws IOException
    {
        String addr = new String("");
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

        addr = address;
        return addr;
    }

    private TextView tv1,tv2,tv3;
    ArrayList<String> l1,l2,l3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_box);

        Intent j = getIntent();
        final Order order = (Order) j.getSerializableExtra("sampleObject");

        String tem = null;
        try {
            tem = getAddress(sharedPrefManager.getInstance(getApplicationContext()).getLatitude(), sharedPrefManager.getInstance(getApplicationContext()).getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
        }

        tv1 = (TextView) findViewById(R.id.odrRest);
        tv1.setText("Order from: " + order.getRestaurantName());

        tv2 = (TextView) findViewById(R.id.odrdelivAddr1);
        tv2.setText("Delivery to:");

        tv3 = (TextView) findViewById(R.id.odrdelivAddr2);
        tv3.setText(tem);

        l1 = new ArrayList<String>();
        l2 = new ArrayList<String>();
        l3 = new ArrayList<String>();

        ArrayList<OrderItem> oitems = order.getOrdered_Items();

        double subtotal = 0.0;

        for (int i = 0; i < oitems.size(); i++) {
            String name = oitems.get(i).getFoodName();
            String amount = Integer.toString(oitems.get(i).getAmount());
            double price = (double) oitems.get(i).getAmount() * oitems.get(i).getFoodPrice();
            String iprice = Double.toString(price);
            subtotal = subtotal + price;
            l1.add(amount);
            l2.add(name);
            l3.add(iprice);
        }

        double tax = subtotal * 0.15;
        double dfee = 60.0;

        double total = subtotal + tax + dfee;

        ArrayAdapter a1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, l1);
        ArrayAdapter a2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, l2);
        ArrayAdapter a3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, l3);

        ListView lv1 = (ListView) findViewById(R.id.odramlist);
        ListView lv2 = (ListView) findViewById(R.id.odrfoodlist);
        ListView lv3 = (ListView) findViewById(R.id.odrpricelist);

        lv1.setAdapter(a1);
        lv2.setAdapter(a2);
        lv3.setAdapter(a3);

        TextView tv2 = (TextView) findViewById(R.id.odrstval);
        TextView tv3 = (TextView) findViewById(R.id.odrtaxval);
        TextView tv4 = (TextView) findViewById(R.id.odrdfeeval);
        TextView tv5 = (TextView) findViewById(R.id.odrtotalval);

        tv2.setText("BDT " + Double.toString(subtotal));
        tv3.setText("BDT " + Double.toString(tax));
        tv4.setText("BDT " + Double.toString(dfee));
        tv5.setText("BDT " + Double.toString(total));


        Button giveOrder = (Button) findViewById(R.id.confodr);

        giveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest s = new StringRequest(Request.Method.POST, constants.lowner_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(orderBox.this, response, Toast.LENGTH_SHORT).show();
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
                        m.put("uid", order.getUserId());
                        m.put("rid", order.getRestaurantId());
                        m.put("Rname", order.getRestaurantName());
                        for (int i = 0; i < order.getUniqueItemNumbers(); i++) {
                            m.put(Integer.toString(i), order.getOrdered_Items().get(i).getOrderItemDesc());
                        }
                        m.put("numbers",Integer.toString(order.getUniqueItemNumbers()));

                        return m;
                    }
                };

                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(s);
            }

        });

    }
}
