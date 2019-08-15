package com.example.foodie_buddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class restaurantFoods extends AppCompatActivity {

    public Order order;
    public TabLayout tb;
    public Button b;

    @Override
    protected void onResume() {
        super.onResume();
        if(tb.getTabCount() >= 2)
        {
            tb.getTabAt(1).select();
            tb.getTabAt(0).select();
        }
        else if(tb.getTabCount() == 1)
        {
            //Toast.makeText(this, Boolean.toString(tb.getTabAt(0).isSelected()), Toast.LENGTH_SHORT).show();
            tb.getTabAt(0).select();
        }
    }

    @Override
    public void onBackPressed() {
        if(order.getOrderStat() != 0)
        {
            new AlertDialog.Builder(this)
                    .setTitle("Exit?")
                    .setMessage("Are you sure you want to exit?\nYour order will be lost")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            order = null;
                            restaurantFoods.super.onBackPressed();
                        }
                    }).create().show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Going backwards", Toast.LENGTH_LONG).show();
            restaurantFoods.super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_foods);

        tb = (TabLayout)findViewById(R.id.foodType);
        final ArrayList<String> types = new ArrayList<String>();
        order = new Order(sharedPrefManager.getInstance(getApplicationContext()).getUserId(),sharedPrefManager.getInstance(getApplicationContext()).getCurrentRestaurantId(),sharedPrefManager.getInstance(getApplicationContext()).getCurrentRestaurant());

        try
        {
            JSONArray J =new JSONArray(sharedPrefManager.getInstance(getApplicationContext()).getCurrentRestaurantFoods());
            for(int i=0;i<J.length();i++)
            {
                JSONObject k = J.getJSONObject(i);
                if(!types.contains(k.getString("type")))
                {
                    types.add(k.getString("type"));
                }
            }

        }
        catch(JSONException x){}

        for(int i=0;i<types.size();i++)
        {
            tb.addTab(tb.newTab().setText(types.get(i)));
        }
        tb.setTabGravity(TabLayout.GRAVITY_FILL);

        tb.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String t = (String)tab.getText();
                final ListView lv = (ListView)findViewById(R.id.foodList);
                final ArrayList<FoodItem> foods = new ArrayList<FoodItem>();

                try
                {
                    JSONArray J =new JSONArray(sharedPrefManager.getInstance(getApplicationContext()).getCurrentRestaurantFoods());
                    for(int i=0;i<J.length();i++)
                    {
                        JSONObject k = J.getJSONObject(i);
                        if(k.getString("type").equalsIgnoreCase(t))
                        {
                            FoodItem f = new FoodItem(k.getInt("id"),k.getString("name"),k.getString("type"),k.getString("description"),k.getDouble("unit_price"));
                            foods.add(f);
                        }
                    }
                }
                catch(JSONException x){}

                foodAdapter adapter = new foodAdapter(foods, getApplicationContext(),order);
                lv.setAdapter(adapter);

                b = (Button)findViewById(R.id.orderBox);
                b.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {

                        if(order.getOrderStat() != 0)
                        {
                            Intent i = new Intent(getApplicationContext(), orderBox.class);
                            i.putExtra("sampleObject",order);
                            startActivity(i);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"No item in the order box", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.menulogout:
                sharedPrefManager.getInstance(this).logout();
                finishAffinity();
                startActivity(new Intent(this,loginActivity.class));
                break;
        }
        return true;
    }
}
