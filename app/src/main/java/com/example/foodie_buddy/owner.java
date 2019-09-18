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

public class owner extends AppCompatActivity {

    private EditText[] t;

    public void registerOwner(View v)
    {
        t = new EditText[3];
        t[0] = (EditText)findViewById(R.id.oname);
        t[1] = (EditText)findViewById(R.id.ownerpassword);
        t[2] = (EditText)findViewById(R.id.owneremail);

        final String s1 = t[0].getText().toString();
        final String s2 = t[1].getText().toString();
        final String s3 = t[2].getText().toString();

        final ProgressDialog p = new ProgressDialog(this);

        if(s1.isEmpty() || s2.isEmpty() || s3.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Provide all the information to register as an owner", Toast.LENGTH_LONG).show();
        }
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(s3).matches())
        {
            Toast.makeText(getApplicationContext(), "Email Id is invalid", Toast.LENGTH_LONG).show();
        }
        else {

            p.setMessage("Registering Owner");
            p.show();

            StringRequest s4 = new StringRequest(Request.Method.POST, constants.cowner_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    p.dismiss();


                    try {
                        JSONObject j = new JSONObject(response);
                        if(!j.getBoolean("estat"))
                        {
                            Toast.makeText(getApplicationContext(), j.getString("info")+"\nNow you can log in", Toast.LENGTH_LONG).show();
                            Intent i = new Intent("com.example.foodie_buddy.ownerLogin");
                            for(int k=0;k<3;k++)
                            {
                                t[k].getText().clear();
                            }
                            startActivity(i);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), j.getString("info"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    p.hide();
                    Toast.makeText(getApplicationContext(), error.getMessage()+"haha", Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> m = new HashMap<>();
                    m.put("name", s1);
                    m.put("email_id", s3);
                    m.put("password", s2);
                    return m;
                }
            };

            RequestHandler.getInstance(this).addToRequestQueue(s4);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);
    }
}