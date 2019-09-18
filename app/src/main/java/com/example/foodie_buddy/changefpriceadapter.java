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

public class changefpriceadapter extends BaseAdapter implements ListAdapter {

	private ArrayList<FoodItem> list = new ArrayList<FoodItem>();
	private Context context;

	public changefpriceadapter(ArrayList<FoodItem> list, Context context) {
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
			view = inflater.inflate(R.layout.changefprice, null);
		}

		//Handle TextView and display string from your list
		TextView fname = (TextView) view.findViewById(R.id.cpfoodName);
		fname.setText(list.get(position).getFoodName());

		TextView fdesc = (TextView) view.findViewById(R.id.cpfoodDesc);
		fdesc.setText(list.get(position).getFoodDesc());

		TextView fprice = (TextView) view.findViewById(R.id.cpfoodPrice);
		fprice.setText(Double.toString(list.get(position).getFoodPrice()) + "tk");

		final Button change = (Button) view.findViewById(R.id.changefoodprice);

		change.setOnClickListener(new View.OnClickListener(){
			@RequiresApi(api = Build.VERSION_CODES.KITKAT)
			@Override
			public void onClick(View v) {
				Intent i = new Intent(parent.getContext(), restModifyOptions.class);
				i.putExtra("foodId",list.get(position).getFoodId());
				parent.getContext().startActivity(i);
				notifyDataSetChanged();
			}
		});

		return view;

	}
}