
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
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class restaurantAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<Restaurants> list = new ArrayList<Restaurants>();
    private Context context;

    public restaurantAdapter(ArrayList<Restaurants> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void showFoods(final ViewGroup parent)
    {
        StringRequest s = new StringRequest(Request.Method.POST, constants.showfoods_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                sharedPrefManager.getInstance(parent.getContext()).saveCurrentRestFoods(response);
                //Toast.makeText(context, sharedPrefManager.getInstance(context).getCurrentRestaurantFoods(), Toast.LENGTH_LONG).show();
                Intent zoom=new Intent(parent.getContext(), restaurantFoods.class);
                parent.getContext().startActivity(zoom);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(parent.getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> m = new HashMap<>();
                m.put("name", sharedPrefManager.getInstance(parent.getContext()).getCurrentRestaurant());
                return m;
            }
        };

        RequestHandler.getInstance(context).addToRequestQueue(s);
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
        return list.get(pos).getId();
    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.restaurantrow, null);
        }

        //Handle TextView and display string from your list
        TextView rname = (TextView)view.findViewById(R.id.resname);
        rname.setText(list.get(position).getName());

        TextView rtype = (TextView)view.findViewById(R.id.restype);
        rtype.setText(list.get(position).getType());

        //Handle buttons and add onClickListeners
        Button addBtn = (Button)view.findViewById(R.id.showmenu);


        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                sharedPrefManager.getInstance(parent.getContext()).saveCurrentRestaurant(list.get(position).getName(),list.get(position).getId());
                sharedPrefManager.getInstance(parent.getContext()).saveOrderResLocation(list.get(position).getLat(),list.get(position).getLng());
                showFoods(parent);
                notifyDataSetChanged();
            }
        });

        return view;
    }
}