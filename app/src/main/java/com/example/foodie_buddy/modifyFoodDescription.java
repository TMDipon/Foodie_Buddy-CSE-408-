package com.example.foodie_buddy;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class modifyFoodDescription extends AppCompatActivity {

    private int foodId,status;
    private TextView tv1;
    private Button btn;
    private EditText t;
    private String temp;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void changeDescription(View v)
    {
        //t = (EditText)findViewById(R.id.description);
        final String tmp = t.getText().toString();
        if(!tmp.isEmpty())
        {
            if (tmp.equalsIgnoreCase(temp))
            {
                t.getText().clear();
                startActivity(new Intent(getApplicationContext(), ownChangeFPrice.class));
                finish();
            }
            else
            {
                StringRequest s = new StringRequest(Request.Method.POST, constants.changeFoodDescription_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Changed the description successfully", Toast.LENGTH_LONG).show();
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
                        m.put("fid", Integer.toString(foodId));
                        m.put("desc", tmp);
                        return m;
                    }
                };

                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(s);
                sharedOwnerManager.getInstance(getApplicationContext()).changeFoodDescriptioninCache(foodId, tmp);
                t.getText().clear();
                startActivity(new Intent(getApplicationContext(), ownChangeFPrice.class));
                finish();
            }
        }
        else
        {
            Toast.makeText(this, "Provide a description to modify the current one", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_food_description);

        Intent j = getIntent();
        foodId = (int) j.getSerializableExtra("foodId");
        status = (int) j.getSerializableExtra("status");

        tv1 = (TextView)findViewById(R.id.changeDescLabel);
        btn = (Button)findViewById(R.id.changed);
        t = (EditText)findViewById(R.id.description);

        if(status == 0)
        {
            tv1.setText("Add food description below");
            btn.setText("Add Description");
        }
        else if(status == 1)
        {
            tv1.setText("Add new description below");
            btn.setText("Change Description");
            temp = sharedOwnerManager.getInstance(getApplicationContext()).getFoodDescriptioninCache(foodId);
            t.setText(temp);
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
