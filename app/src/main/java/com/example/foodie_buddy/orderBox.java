package com.example.foodie_buddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class orderBox extends AppCompatActivity {

    public TextView tv1;
    ArrayList<String> l1,l2,l3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_box);

        Intent j = getIntent();
        Order order = (Order)j.getSerializableExtra("sampleObject");

        tv1 = (TextView)findViewById(R.id.odrRest);
        tv1.setText("Order from: "+order.getRestaurantName());

        l1 = new ArrayList<String>();
        l2 = new ArrayList<String>();
        l3 = new ArrayList<String>();

        ArrayList<OrderItem> oitems = order.getOrdered_Items();

        double subtotal = 0.0;

        for(int i=0;i<oitems.size();i++)
        {
            String name = oitems.get(i).getFoodName();
            String amount = Integer.toString(oitems.get(i).getAmount());
            double price = (double)oitems.get(i).getAmount() * oitems.get(i).getFoodPrice();
            String iprice = Double.toString(price);
            subtotal = subtotal + price;
            l1.add(amount);
            l2.add(name);
            l3.add(iprice);
        }

        double tax = subtotal * 0.15;
        double dfee = 60.0;

        double total = subtotal + tax + dfee;

        ArrayAdapter a1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, l1);
        ArrayAdapter a2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, l2);
        ArrayAdapter a3 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, l3);

        ListView lv1 = (ListView)findViewById(R.id.odramlist);
        ListView lv2 = (ListView)findViewById(R.id.odrfoodlist);
        ListView lv3 = (ListView)findViewById(R.id.odrpricelist);

        lv1.setAdapter(a1);
        lv2.setAdapter(a2);
        lv3.setAdapter(a3);

        TextView tv2 = (TextView)findViewById(R.id.odrstval);
        TextView tv3 = (TextView)findViewById(R.id.odrtaxval);
        TextView tv4 = (TextView)findViewById(R.id.odrdfeeval);
        TextView tv5 = (TextView)findViewById(R.id.odrtotalval);

        tv2.setText("BDT "+Double.toString(subtotal));
        tv3.setText("BDT "+Double.toString(tax));
        tv4.setText("BDT "+Double.toString(dfee));
        tv5.setText("BDT "+Double.toString(total));

    }
}
