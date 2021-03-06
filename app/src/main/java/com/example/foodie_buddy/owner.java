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

        final HashMap<String, String> map = new HashMap<>();

        map.put("name",t[0].getText().toString());
        map.put("password",t[1].getText().toString());
        map.put("email",t[2].getText().toString());

        final ProgressDialog p = new ProgressDialog(this);

        String [] status = validateRegistration.validateRegFields(map);

        if(status[0].equals("0"))
        {
            Toast.makeText(getApplicationContext(), status[1], Toast.LENGTH_LONG).show();
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
                            /*
                            for(int k=0;k<3;k++)
                            {
                                t[k].getText().clear();
                            }
                            */
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
                    Toast.makeText(getApplicationContext(), error.getMessage()+" error in response", Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> m = new HashMap<>();
                    m.put("name", map.get("name"));
                    m.put("email_id", map.get("email"));
                    m.put("password", map.get("password"));
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