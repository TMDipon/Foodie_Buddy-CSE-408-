package com.example.foodie_buddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 3)
        {
            finish();
        }
    }

    public void User(View v)
    {
        Intent i = new Intent("com.example.foodie_buddy.loginActivity");
        startActivityForResult(i, 3);
        //startActivity(i);
    }

    public void Owner(View v)
    {
        Intent i = new Intent("com.example.foodie_buddy.ownerLogin");
        startActivityForResult(i, 3);
        //startActivity(i);
    }

    public void Rider(View v)
    {
        //Intent i = new Intent("com.example.foodie_buddy.riderLogin");
        //startActivity(i);
        Intent i = new Intent(getApplicationContext(),riderLogin.class);
        startActivityForResult(i, 3);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        constants.make();

        if(sharedPrefManager.getInstance(this).isLoggedIn())
        {
            finish();
            startActivity(new Intent(this, userProfile.class));
        }
        else if(sharedOwnerManager.getInstance(this).isOwnerLoggedIn())
        {
            finish();
            startActivity(new Intent(this, ownerProfile.class));
        }
        else if(sharedRiderManager.getInstance(this).isRiderLoggedIn())
        {
            finish();
            startActivity(new Intent(this, riderProfile.class));
        }

        getSupportActionBar().hide();
    }
}
