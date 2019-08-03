package com.example.foodie_buddy;

import android.content.Context;
import android.content.Intent;
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

public class foodAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<FoodItem> list = new ArrayList<FoodItem>();
    private Context context;
    private Order order; //This will be the order given

    public foodAdapter(ArrayList<FoodItem> list, Context context, Order o) {
        this.list = list;
        this.context = context;
        order = o;
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
        return list.get(pos).getFoodId();
    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.foodrow, null);
        }

        //Handle TextView and display string from your list
        TextView fname = (TextView)view.findViewById(R.id.foodName);
        fname.setText(list.get(position).getFoodName());

        TextView fdesc = (TextView)view.findViewById(R.id.foodDesc);
        fdesc.setText(list.get(position).getFoodDesc());

        TextView fprice = (TextView)view.findViewById(R.id.foodPrice);
        fprice.setText(Double.toString(list.get(position).getFoodPrice())+"tk");

        //Handle buttons and add onClickListeners
        final Button add = (Button)view.findViewById(R.id.Addfood);

        final Button plus = (Button)view.findViewById(R.id.plus);
        final Button minus = (Button)view.findViewById(R.id.minus);

        if(order.findFood(list.get(position).getFoodId()) != -1)
        {
            plus.setVisibility(View.VISIBLE);
            minus.setVisibility(View.VISIBLE);
            add.setEnabled(false);
            add.setText(Integer.toString(order.getOrderItemAmount(list.get(position).getFoodId())));
        }

        plus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int cam = order.getOrderItemAmount(list.get(position).getFoodId());
                if(cam < 10) {
                    int am = order.Add(list.get(position).getFoodId(), list.get(position).getFoodName(), list.get(position).getFoodPrice());
                    String tmp = list.get(position).getFoodName();
                    add.setText(Integer.toString(am));
                    //Toast.makeText(parent.getContext(), tmp + " added\nTotal " + Integer.toString(am) + " " + tmp + " in the order box", Toast.LENGTH_LONG).show();
                    notifyDataSetChanged();
                }
                else
                {
                    Toast.makeText(parent.getContext(), "Can't add more of this item, max limit reached", Toast.LENGTH_LONG).show();
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int am = order.Add(list.get(position).getFoodId(),list.get(position).getFoodName(),list.get(position).getFoodPrice());
                String tmp = list.get(position).getFoodName();
                plus.setVisibility(View.VISIBLE);
                minus.setVisibility(View.VISIBLE);
                add.setText(Integer.toString(am));
                add.setEnabled(false);
                //Toast.makeText(parent.getContext(),tmp+" added\nTotal "+Integer.toString(am)+" "+tmp+" in the order box", Toast.LENGTH_LONG).show();
                notifyDataSetChanged();
            }
        });

        minus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int tmp = order.Remove(list.get(position).getFoodId());
                if( tmp == 2 )
                {
                    plus.setVisibility(View.GONE);
                    minus.setVisibility(View.GONE);
                    add.setText("Add");
                    add.setEnabled(true);
                }
                else if( tmp == 0 )
                {
                    Toast.makeText(parent.getContext(), list.get(position).getFoodName()+" not present in the order box", Toast.LENGTH_LONG).show();
                }
                else
                {
                    add.setText(Integer.toString(order.getOrderItemAmount(list.get(position).getFoodId())));
                    //Toast.makeText(parent.getContext(), list.get(position).getFoodName()+" removed from the order box", Toast.LENGTH_LONG).show();
                }
                notifyDataSetChanged();
            }
        });

        return view;
    }
}
