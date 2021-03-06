package com.example.foodie_buddy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class riderLogin extends AppCompatActivity {

    EditText t1,t2;
    public String name, eid;

    public void riderSign_in(View v)
    {
        startActivity(new Intent(getApplicationContext(),rider.class));
    }

    public void riderLog_in(View v)
    {

        t1 = (EditText)findViewById(R.id.riderlogmail);
        t2 = (EditText)findViewById(R.id.riderlogpass);

        final String s1 = t1.getText().toString();
        final String s2 = t2.getText().toString();

        final ProgressDialog p = new ProgressDialog(this);

        if(s1.isEmpty() || s2.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Provide necessary information to login", Toast.LENGTH_LONG).show();
        }
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(s1).matches())
        {
            Toast.makeText(getApplicationContext(), "Email Id is invalid", Toast.LENGTH_LONG).show();
        }
        else {

            p.setMessage("Please Wait....");
            p.show();

            StringRequest s3 = new StringRequest(Request.Method.POST, constants.lrider_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    p.dismiss();

                    try {
                        JSONObject j = new JSONObject(response);
                        if(j.getBoolean("estat"))
                        {
                            Toast.makeText(getApplicationContext(), j.getString("info"), Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            sharedRiderManager.getInstance(getApplicationContext()).riderLogin(response);
                            Toast.makeText(getApplicationContext(), j.getString("info"), Toast.LENGTH_SHORT).show();
                            t1.getText().clear();
                            t2.getText().clear();
                            startActivity(new Intent(getApplicationContext(),riderProfile.class));
                            finish();
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
                    m.put("email_id", s1);
                    m.put("password", s2);
                    return m;
                }
            };

            RequestHandler.getInstance(this).addToRequestQueue(s3);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_login);
    }
}
