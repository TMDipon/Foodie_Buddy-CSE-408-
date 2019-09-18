package com.example.foodie_buddy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class delfoodAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<FoodItem> list = new ArrayList<FoodItem>();
    private Context context;

    public delfoodAdapter(ArrayList<FoodItem> list, Context context) {
        this.list = list;
        this.context = context;
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
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.delfoodrow, null);
        }

        //Handle TextView and display string from your list
        TextView fname = (TextView) view.findViewById(R.id.dfoodName);
        fname.setText(list.get(position).getFoodName());

        TextView fdesc = (TextView) view.findViewById(R.id.dfoodDesc);
        fdesc.setText(list.get(position).getFoodDesc());

        TextView fprice = (TextView) view.findViewById(R.id.dfoodPrice);
        fprice.setText(Double.toString(list.get(position).getFoodPrice()) + "tk");

        final Button remove = (Button) view.findViewById(R.id.delete);

        remove.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(parent.getContext())
                        .setTitle("Remove Item?")
                        .setMessage("Are you sure you want to remove the chosen food item?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {

                                StringRequest s = new StringRequest(Request.Method.POST, constants.deleteFood_URL, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Toast.makeText(parent.getContext(), "Successfully Removed the food item", Toast.LENGTH_LONG).show();
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
                                        m.put("rid", Integer.toString(sharedOwnerManager.getInstance(parent.getContext()).getCurrentResId()));
                                        m.put("fid",Integer.toString(list.get(position).getFoodId()));
                                        return m;
                                    }
                                };

                                RequestHandler.getInstance(parent.getContext()).addToRequestQueue(s);
                                sharedOwnerManager.getInstance(parent.getContext()).deleteFoodinCache(list.get(position).getFoodId());
                                parent.getContext().startActivity(new Intent(parent.getContext(),ownDeleteFood.class));
                                ((Activity) parent.getContext()).finish();

                            }
                        }).create().show();

                notifyDataSetChanged();
            }
        });


        return view;

    }

}