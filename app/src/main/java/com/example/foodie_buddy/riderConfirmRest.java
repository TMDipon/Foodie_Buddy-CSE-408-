package com.example.foodie_buddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class riderConfirmRest extends AppCompatActivity {
    private TextView tv1;
    private ListView x;
    private ArrayList<String> l1;

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please complete all of your tasks first to deliver this order", Toast.LENGTH_SHORT).show();
    }

    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }

    public void makeCall(View v)
    {
        dialContactPhone(sharedRiderManager.getInstance(getApplicationContext()).getRestPhone());
    }

    public void confirmed(View v)
    {
        finish();
        startActivity(new Intent(getApplicationContext(),riderReachRest.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_confirm_rest);

        tv1 = (TextView)findViewById(R.id.restphnnum);
        tv1.setText(sharedRiderManager.getInstance(getApplicationContext()).getRestPhone());

        x = (ListView)findViewById(R.id.odritems1);
        l1 = sharedRiderManager.getInstance(getApplicationContext()).getOrderItems();
        ArrayAdapter a1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, l1);
        x.setAdapter(a1);
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
                sharedRiderManager.getInstance(this).riderlogout();
                finishAffinity();
                startActivity(new Intent(this,riderLogin.class));
                break;
        }
        return true;
    }
}
