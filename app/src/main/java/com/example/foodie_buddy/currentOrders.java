package com.example.foodie_buddy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ListView;

import java.util.ArrayList;

public class currentOrders extends AppCompatActivity {

    private ListView lv;
    private orderitemadapter x;
    private ArrayList<Order> order,order1;
    public CountDownTimer tmr;

    @Override
    public void onBackPressed() {
        finish();
        currentOrders.super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_orders);

        lv = (ListView)findViewById(R.id.orderList);
        order = orderManager.getCurrentOrders();
        order1 = new ArrayList<Order>();
        for(int i=0;i<order.size();i++)
        {
            if(!order.get(i).getOrderStatus().equalsIgnoreCase("Cancelled as no rider found"))
            {
                order1.add(order.get(i));
            }
        }

        x = new orderitemadapter(order1, getApplicationContext());
        lv.setAdapter(x);
    }
}
