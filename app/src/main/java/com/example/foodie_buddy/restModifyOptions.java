package com.example.foodie_buddy;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class restModifyOptions extends AppCompatActivity {

    private int foodId;
    private Button btn;
    private String desc;

    public void changePrice(View v)
    {
        Intent intent = new Intent(getApplicationContext(), ChangeFoodPrice.class);
        intent.putExtra("foodId",foodId);
        startActivity(intent);
    }

    public void monitorDescription(View v)
    {
        Intent intent = new Intent(getApplicationContext(), modifyFoodDescription.class);
        intent.putExtra("foodId",foodId);
        if(desc.equalsIgnoreCase("NA"))
        {
            intent.putExtra("status",0);
            startActivity(intent);
        }
        else
        {
            intent.putExtra("status",1);
            startActivity(intent);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void deleteFood(View v)
    {
        String foodName = sharedOwnerManager.getInstance(getApplicationContext()).getFoodNameinCache(foodId);
        new AlertDialog.Builder(this)
                .setTitle("Exit?")
                .setMessage("Are you sure you want to Delete?\n   "+foodName)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        StringRequest s = new StringRequest(Request.Method.POST, constants.deleteFood_URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getApplicationContext(), "Successfully Removed the food item", Toast.LENGTH_LONG).show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> m = new HashMap<>();
                                m.put("rid", Integer.toString(sharedOwnerManager.getInstance(getApplicationContext()).getCurrentResId()));
                                m.put("fid",Integer.toString(foodId));
                                return m;
                            }
                        };

                        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(s);
                        sharedOwnerManager.getInstance(getApplicationContext()).deleteFoodinCache(foodId);
                        startActivity(new Intent(getApplicationContext(),ownChangeFPrice.class));
                        finish();
                    }
                }).create().show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_modify_options);

        Intent j = getIntent();
        foodId = (int) j.getSerializableExtra("foodId");

        btn = (Button)findViewById(R.id.monitorDescription);

        desc = sharedOwnerManager.getInstance(getApplicationContext()).getFoodDescriptioninCache(foodId);
        if(!desc.equalsIgnoreCase("NA"))
        {
            btn.setText("Change Description");
        }
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
