
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

public class ChangeFoodPrice extends AppCompatActivity {

    private int fid;
    private EditText t;
    private TextView tv;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_food_price);

        Intent j = getIntent();
        fid = (int) j.getSerializableExtra("foodId");
        tv = (TextView)findViewById(R.id.curpriceval);
        tv.setText("BDT "+Double.toString(sharedOwnerManager.getInstance(getApplicationContext()).getFoodPriceinCache(fid)));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void changePrice(View v)
    {
        t = (EditText)findViewById(R.id.cfp);
        final String tmp = t.getText().toString();
        if(!tmp.isEmpty()) {
            StringRequest s = new StringRequest(Request.Method.POST, constants.changeFoodPrice_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(getApplicationContext(), "Changed the price successfully", Toast.LENGTH_LONG).show();
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
                    m.put("fid", Integer.toString(fid));
                    m.put("price",tmp);
                    return m;
                }
            };

            RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(s);
            sharedOwnerManager.getInstance(getApplicationContext()).changeFoodPriceinCache(fid,Double.parseDouble(tmp));
            t.getText().clear();
            startActivity(new Intent(getApplicationContext(),ownChangeFPrice.class));
            finish();
        }
        else
        {
            Toast.makeText(this, "Provide a price to change the current one", Toast.LENGTH_SHORT).show();
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