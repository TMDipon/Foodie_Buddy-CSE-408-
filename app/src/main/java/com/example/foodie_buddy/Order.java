package com.example.foodie_buddy;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Order extends Activity implements Serializable{
    private int RestaurantId;
    private int UserId;
    private String RestaurantName;
    private ArrayList<OrderItem> ordered_Items;
    private int Order_id;
    private String Order_Status;
    private CountDownTimer countDownTimer;
    int tickCount;

    public Order(int x, int y, String z) {
        UserId = x;
        RestaurantId = y;
        RestaurantName = z;
        ordered_Items = new ArrayList<OrderItem>();
        tickCount = 0;
        Order_Status = "NA";
    }

    public Order(){}

    public void cancel()
    {
        countDownTimer.cancel();
    }

    public void start(final Context ctx)
    {
        countDownTimer = new CountDownTimer(600000, 8000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tickCount++;
                if(!getOrderStatus().equalsIgnoreCase("Order Delivered, Task Complete")) {
                    StringRequest s3 = new StringRequest(Request.Method.POST, constants.getOrderStatus_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String temp = "";

                            try {
                                JSONArray J = new JSONArray(response);

                                for (int i = 0; i < J.length(); i++) {
                                    JSONObject j = J.getJSONObject(i);
                                    temp = j.getString("order_status");
                                    if (temp.equalsIgnoreCase("Order Delivered, Task Complete")) {
                                        setOrderStatus(temp);
                                        sharedPrefManager.getInstance(ctx).saveOrderCompletetion(1);
                                        sharedPrefManager.getInstance(ctx).saveOrderNo(getOrderId());

                                        if(sharedPrefManager.getInstance(ctx).isLoggedIn())
                                        {
                                            Intent in = new Intent(ctx, foodConfirmed.class);
                                            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            ctx.startActivity(in);
                                        }
                                    }
                                    else if(tickCount == 10 && temp.equalsIgnoreCase("Looking for Rider"))
                                    {
                                        tickCount = 0;

                                        StringRequest s = new StringRequest(Request.Method.POST, constants.updateOrderStatusonCancel_URL, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) { }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(getApplicationContext(), error.toString() + " vollyerror", Toast.LENGTH_LONG).show();
                                            }
                                        }) {
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> m = new HashMap<>();
                                                m.put("oid", Integer.toString(getOrderId()));
                                                m.put("order_stat", "Cancelled as no rider found");

                                                return m;
                                            }
                                        };

                                        RequestHandler.getInstance(ctx).addToRequestQueue(s);
                                        setOrderStatus("Cancelled as no rider found");
                                        sharedPrefManager.getInstance(ctx).saveOrderCompletetion(2);
                                        sharedPrefManager.getInstance(ctx).saveOrderNo(getOrderId());

                                        if(sharedPrefManager.getInstance(ctx).isLoggedIn())
                                        {
                                            Intent in = new Intent(ctx, userProfile.class);
                                            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            ctx.startActivity(in);
                                        }
                                    }
                                    else
                                    {
                                        //setOrderStatus(temp);
                                        if(sharedPrefManager.getInstance(ctx).isLoggedIn() && !getOrderStatus().equalsIgnoreCase(temp))
                                        {
                                            setOrderStatus(temp);
                                            sharedPrefManager.getInstance(ctx).saveOrderCompletetion(3);
                                            sharedPrefManager.getInstance(ctx).saveOrderNo(getOrderId());
                                            Intent in = new Intent(ctx, userProfile.class);
                                            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            ctx.startActivity(in);
                                        }
                                        else
                                        {
                                            setOrderStatus(temp);
                                        }

                                    }
                                }

                            } catch (JSONException e) {
                                Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ctx, error.getMessage() + "haha", Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> m = new HashMap<>();
                            m.put("id", Integer.toString(Order_id));
                            return m;
                        }
                    };

                    RequestHandler.getInstance(ctx).addToRequestQueue(s3);
                }
            }

            @Override
            public void onFinish() {
                Toast.makeText(ctx, "Timer ends", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    public void setOrderId(int id)
    {
        this.Order_id = id;
    }

    public int getOrderId()
    {
        return this.Order_id;
    }

    public void setOrderStatus(String temp)
    {
        this.Order_Status = temp;
    }

    public String getOrderStatus(){ return this.Order_Status; }

    public String getRestaurantName() {
        return this.RestaurantName;
    }

    public String getRestaurantId() {
        return Integer.toString(this.RestaurantId);
    }

    public String getUserId() {
        return Integer.toString(this.UserId);
    }

    public ArrayList<OrderItem> getOrdered_Items() {
        return ordered_Items;
    }

    public int getUniqueItemNumbers() {
        return this.ordered_Items.size();
    }

    public int getOrderStat() {
        if (!ordered_Items.isEmpty()) {
            return ordered_Items.size();
        }
        return 0;
    }

    public double getSubTotal()
    {
        double subtotal = 0.0;
        for(int i=0;i<ordered_Items.size();i++)
        {
            subtotal += ordered_Items.get(i).getAmount() * ordered_Items.get(i).getFoodPrice();
        }
        return subtotal;
    }

    public int getOrderItemAmount(int foodId) {
        int i = findFood(foodId);
        if (i != -1) {
            return ordered_Items.get(i).getAmount();
        }
        return 0;
    }

    public int findFood(int x) {
        for (int i = 0; i < ordered_Items.size(); i++) {
            if (ordered_Items.get(i).getFoodId() == x) {
                return i;
            }
        }
        return -1;
    }

    public int Add(int foodId, String foodName, double price) {
        int stat = findFood(foodId);
        if (stat == -1) {
            OrderItem tem = new OrderItem(foodId, foodName, price);
            ordered_Items.add(tem);
        } else {
            ordered_Items.get(stat).addItem();
            return ordered_Items.get(stat).getAmount();
        }

        return 1;
    }

    public int Remove(int foodId) {
        int stat = findFood(foodId);
        int am = -1;
        if (stat != -1)      //means the food is added so can be removed
        {
            ordered_Items.get(stat).removeItem();
            am = ordered_Items.get(stat).getAmount();//see the amount of this item, if 0 then remove from the list

            if (am == 0) {
                ordered_Items.remove(stat);
                return 2;       //means the food is no longer in the cart
            }
            return 1;       //means some amount of this food is still in the cart
        }
        return 0;
    }
}
