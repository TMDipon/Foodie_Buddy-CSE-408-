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

    public void User(View v)
    {
        Intent i = new Intent("com.example.foodie_buddy.loginActivity");
        startActivity(i);
    }

    public void Owner(View v)
    {
        Intent i = new Intent("com.example.foodie_buddy.ownerLogin");
        startActivity(i);
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
    }
}
