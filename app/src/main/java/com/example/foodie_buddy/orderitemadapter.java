package com.example.foodie_buddy;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class orderitemadapter extends BaseAdapter implements ListAdapter {
    private ArrayList<Order> list = new ArrayList<Order>();
    private Context context;
    private CountDownTimer timer;
    private TextView status;

    public orderitemadapter(ArrayList<Order> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void cancelTimer()
    {
        timer.cancel();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return list.get(pos).getOrderId();
    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.orderitem, null);
        }

            timer = new CountDownTimer(3600000, 5000) {
                @Override
                public void onTick(long l) {
                    status.setText("Status: " + list.get(position).getOrderStatus());
                }

                @Override
                public void onFinish() {

                }
            }.start();

        //Handle TextView and display string from your list
        TextView rname = (TextView)view.findViewById(R.id.orderRest);
        rname.setText("Order from: "+list.get(position).getRestaurantName());

        status = (TextView)view.findViewById(R.id.orderStatus);
        status.setText(list.get(position).getOrderStatus());

        /*
        //Handle buttons and add onClickListeners
        Button btn = (Button)view.findViewById(R.id.orderItems);


        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                sharedPrefManager.getInstance(parent.getContext()).saveOrderNo(position);
                notifyDataSetChanged();
            }
        });
        */

        return view;
    }
}
