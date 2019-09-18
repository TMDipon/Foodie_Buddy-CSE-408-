package com.example.foodie_buddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class foodConfirmed extends AppCompatActivity {

    private TextView tv;
    private Order tem;
    private ArrayList<String> l1;
    private ListView x;
    private int idx;

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please pay your payment first\nThen you can give order", Toast.LENGTH_LONG).show();
    }

    public void pay(View v)
    {
        orderManager.Remove(idx);
        sharedPrefManager.getInstance(getApplicationContext()).saveOrderCompletetion(0);
        startActivity(new Intent((getApplicationContext()),userProfile.class));
        finish();
    }

    public void notification(String tem1)
    {
        NotificationManager notificationManager = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = "channel1";
            String description = "Created for notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "1")
                .setContentTitle("Order Completed")
                .setContentText(tem1)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(tem1))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setAutoCancel(true);

        notificationManager.notify(0, notification.build());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_confirmed);

        int orderId = sharedPrefManager.getInstance(getApplicationContext()).getOrderNo();
        idx = orderManager.getOrderIndex(orderId);

        tem = orderManager.getOrder(idx);
        tem.cancel();
        notification(tem.getOrderStatus());

        ArrayList<OrderItem> oitems = tem.getOrdered_Items();
        double subtotal = tem.getSubTotal();
        double vat = subtotal * 0.15;
        double dfee = 60.0;

        double total = subtotal + vat + dfee;

        tv = (TextView)findViewById(R.id.userPayVal1);
        tv.setText("BDT: "+Double.toString(Math.ceil(total)));

        l1 = new ArrayList<String>();
        for(int i=0;i<oitems.size();i++)
        {
            l1.add(oitems.get(i).getItemDesc());
        }

        ArrayAdapter a1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, l1);
        x = (ListView)findViewById(R.id.userOrderItems);
        x.setAdapter(a1);
    }
}
