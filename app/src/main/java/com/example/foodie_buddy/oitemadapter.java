package com.example.foodie_buddy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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

public class oitemadapter extends BaseAdapter implements ListAdapter {

    private ArrayList<String> list = new ArrayList<String>();
    private final int[] list1;
    private Context context;
    private int stat;

    public oitemadapter(ArrayList<String> list, Context context, int x) {
        this.list = list;
        this.context = context;
        this.stat = x;
        list1 = new int[list.size()];
        for(int i=0;i<list.size();i++)
        {
            list1[i] = 0;
        }
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
        return pos;
    }

    public boolean allChecked()
    {
        boolean flag = true;
        for(int i= 0;i < list.size();i++)
        {
            if(list1[i] == 0)
            {
                flag = false;
                break;
            }
        }

        return  flag;
    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.oitemrow, null);
        }

        //Handle TextView and display string from your list
        TextView itemDetail = (TextView)view.findViewById(R.id.oitemdetail);
        itemDetail.setText(list.get(position));

        CheckBox ch = (CheckBox)view.findViewById(R.id.checkPicked);
        if(stat == 1)
        {
            ch.setText("Received");
        }

        ch.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(((CheckBox)v).isChecked())
                        {
                            list1[position] = 1;
                        }
                        else
                        {
                            list1[position] = 0;
                        }
                    }
                }
        );

        return view;
    }
}
