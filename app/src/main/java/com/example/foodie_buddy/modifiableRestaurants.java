package com.example.foodie_buddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class modifiableRestaurants extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifiable_restaurants);

        final ListView lv = (ListView)findViewById(R.id.restUList);
        ArrayList<String> restaurantsUnder = new ArrayList<String>();
        final ArrayList<uRestaurant> uRests = new ArrayList<uRestaurant>();
        final ArrayAdapter<String>[] temp = new ArrayAdapter[1];

        try
        {
            JSONArray J =new JSONArray(sharedOwnerManager.getInstance(getApplicationContext()).getRestaurantsUnder());
            for(int i=0;i<J.length();i++)
            {
                JSONObject k = J.getJSONObject(i);
                restaurantsUnder.add(k.getString("name"));
                uRests.add(new uRestaurant(k.getString("name"),k.getString("type"),k.getInt("id")));
            }
        }
        catch(JSONException x){}

        temp[0] = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,restaurantsUnder);
        lv.setAdapter(temp[0]);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //adapterView: A generic class object that refers to the parent view(here lv) on which it is working
                //view: the row that will be tabbed
                //position: position of the row that will be tabbed
                sharedOwnerManager.getInstance(getApplicationContext()).current_Rest(uRests.get(position).getId(),uRests.get(position).getType());
                startActivity(new Intent(getApplicationContext(),mrAddFood.class));
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
