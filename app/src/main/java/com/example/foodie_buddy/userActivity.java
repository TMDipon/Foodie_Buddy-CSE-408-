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

public class userActivity extends AppCompatActivity {

    public EditText t1,t2,t3,t4;

    public void register(View v)
    {
        t1 = (EditText)findViewById(R.id.username);
        t2 = (EditText)findViewById(R.id.password);
        t3 = (EditText)findViewById(R.id.email);
        t4 = (EditText)findViewById(R.id.phone);

        final String s1 = t1.getText().toString().trim();
        final String s2 = t2.getText().toString().trim();
        final String s3 = t3.getText().toString().trim();
        final String s5 = t4.getText().toString().trim();

        String temp = "";

        if(!s5.isEmpty()){
            temp = s5.substring(0,3);
        }

        final ProgressDialog p = new ProgressDialog(this);

        if(s1.isEmpty() || s2.isEmpty() || s3.isEmpty() || s5.isEmpty())
        {
            Toast.makeText(userActivity.this, "Provide all the information to register", Toast.LENGTH_LONG).show();
        }
        else if(s5.length() != 11)
        {
            Toast.makeText(userActivity.this, "Wrong phone number length", Toast.LENGTH_LONG).show();
        }
        else if(!(temp.equals("013") || temp.equals("014") || temp.equals("015") || temp.equals("016") || temp.equals("017") || temp.equals("018") || temp.equals("019")))
        {
            Toast.makeText(userActivity.this, "Invalid phone number", Toast.LENGTH_LONG).show();
        }
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(s3).matches())
        {
            Toast.makeText(getApplicationContext(), "Email Id is invalid", Toast.LENGTH_LONG).show();
        }
        else {

            p.setMessage("Registering User");
            p.show();

            StringRequest s4 = new StringRequest(Request.Method.POST, constants.cuser_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    p.dismiss();


                    try {
                        JSONObject j = new JSONObject(response);
                        if(!j.getBoolean("estat"))
                        {
                            Toast.makeText(userActivity.this, j.getString("info"), Toast.LENGTH_LONG).show();
                            t1.getText().clear();
                            t2.getText().clear();
                            t3.getText().clear();
                            t4.getText().clear();
                            startActivity(new Intent(getApplicationContext(),loginActivity.class));
                            finish();
                        }
                        else
                        {
                            Toast.makeText(userActivity.this, j.getString("info"), Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        Toast.makeText(userActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    p.hide();
                    Toast.makeText(userActivity.this, error.getMessage()+"haha", Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> m = new HashMap<>();
                    m.put("name", s1);
                    m.put("email_id", s3);
                    m.put("phone",s5);
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
        setContentView(R.layout.activity_user);
    }
}
