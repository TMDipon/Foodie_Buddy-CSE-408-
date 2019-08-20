package com.example.foodie_buddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FoodInsert extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public EditText[] arr;
    public Spinner foodType;
    private String typ="nil";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_insert);

        foodType = findViewById(R.id.ftype);//spinner for food types
        ArrayAdapter<String> s= new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, constants.mp.get(sharedOwnerManager.getInstance(getApplicationContext()).getCurrentResType()));
        s.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foodType.setAdapter(s);
        foodType.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch(adapterView.getId()){
            case R.id.ftype :
                if(!adapterView.getItemAtPosition(i).toString().equals("Type"))
                {
                    typ = adapterView.getItemAtPosition(i).toString();
                }
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void addFood(View v)
    {
        arr = new EditText[3];
        arr[0] = (EditText)findViewById(R.id.fname);
        arr[1] = (EditText)findViewById(R.id.fdesc);
        arr[2] = (EditText)findViewById(R.id.fprice);

        final String s1 = arr[0].getText().toString();
        final String s3 = arr[2].getText().toString();
        final String s2;

        if(arr[1].getText().toString().trim().isEmpty())
        {
            s2 = "NA";
        }
        else
        {
            s2 = arr[1].getText().toString();
        }

        final String tem = Integer.toString(sharedOwnerManager.getInstance(this).getCurrentResId());

        final ProgressDialog p = new ProgressDialog(this);

        if(s1.isEmpty() || s3.isEmpty() || typ.equals("nil"))
        {
            Toast.makeText(getApplicationContext(), "Provide or select necessary information for adding food", Toast.LENGTH_LONG).show();
        }
        else {

            p.setMessage("Adding, Please Wait....");
            p.show();

            StringRequest s7 = new StringRequest(Request.Method.POST, constants.addFood_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    p.dismiss();

                    try {
                        JSONObject j = new JSONObject(response);
                        if (!j.getBoolean("estat")) {

                            Toast.makeText(getApplicationContext(), j.getString("info"), Toast.LENGTH_SHORT).show();
                            foodType.setSelection(0);
                            typ = "nil";
                            arr[0].getText().clear();
                            arr[1].getText().clear();
                            arr[2].getText().clear();
                        } else {
                            Toast.makeText(getApplicationContext(), j.getString("info"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    p.hide();
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> m = new HashMap<>();
                    m.put("rid", tem);
                    m.put("name", s1);
                    m.put("type", typ);
                    m.put("desc", s2);
                    m.put("uprice", s3);
                    return m;
                }
            };

            RequestHandler.getInstance(this).addToRequestQueue(s7);

        }
    }
}