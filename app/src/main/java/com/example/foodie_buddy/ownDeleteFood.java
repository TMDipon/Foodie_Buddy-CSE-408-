package com.example.foodie_buddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ownDeleteFood extends AppCompatActivity {

    public TabLayout tb;

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
            tb.addTab(tb.newTab().setText("Temp"));
            tb.getTabAt(1).select();
            tb.getTabAt(0).select();
            tb.removeTabAt(1);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_delete_food);

        tb = (TabLayout)findViewById(R.id.delfoodType);
        final ArrayList<String> types = new ArrayList<String>();

        try
        {
            JSONArray J =new JSONArray(sharedOwnerManager.getInstance(getApplicationContext()).getFoodsUnder());
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
                final ListView lv = (ListView)findViewById(R.id.delfoodList);
                final ArrayList<FoodItem> foods = new ArrayList<FoodItem>();

                try
                {
                    JSONArray J =new JSONArray(sharedOwnerManager.getInstance(getApplicationContext()).getFoodsUnder());
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

                delfoodAdapter adapter = new delfoodAdapter(foods, getApplicationContext());
                lv.setAdapter(adapter);
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
                sharedOwnerManager.getInstance(this).logout();
                finishAffinity();
                startActivity(new Intent(this,ownerLogin.class));
                break;
        }
        return true;
    }
}
