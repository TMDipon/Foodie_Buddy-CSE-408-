package com.example.foodie_buddy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class rider extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private String dis="nil";
    private String ar="nil";

    private Spinner spin_district,spin_area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider);

        spin_district = findViewById(R.id.rdistrict);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,constants.districts.get("Bangladesh"));
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_district.setAdapter(adapter1);
        spin_district.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        spin_area = findViewById(R.id.rarea);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,constants.areas.get(spin_district.getSelectedItem().toString()));
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_area.setAdapter(adapter4);
        spin_area.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch(adapterView.getId()){
            case R.id.rdistrict :
                if(!adapterView.getItemAtPosition(i).toString().equals("District"))
                {
                    dis = adapterView.getItemAtPosition(i).toString();
                    ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,constants.areas.get(dis));
                    adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin_area.setAdapter(adapter4);
                    Toast.makeText(getApplicationContext(), dis, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rarea :
                if(!adapterView.getItemAtPosition(i).toString().equals("Area"))
                {
                    ar = adapterView.getItemAtPosition(i).toString();
                    Toast.makeText(getApplicationContext(), ar, Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private EditText t1,t2,t3,t4,t5;

    public void riderRegister(View v)
    {
        t1 = (EditText)findViewById(R.id.ridername);
        t2 = (EditText)findViewById(R.id.riderpassword);
        t3 = (EditText)findViewById(R.id.rideremail);
        t4 = (EditText)findViewById(R.id.riderphone);
        t5 = (EditText)findViewById(R.id.ridernid);

        final String s1 = t1.getText().toString().trim();
        final String s2 = t2.getText().toString().trim();
        final String s3 = t3.getText().toString().trim();
        final String s5 = t4.getText().toString().trim();
        final String s6 = t5.getText().toString().trim();

        String temp = "";

        if(!s5.isEmpty()){
            temp = s5.substring(0,3);
        }

        final ProgressDialog p = new ProgressDialog(this);

        if(s1.isEmpty() || s2.isEmpty() || s3.isEmpty() || s5.isEmpty() || s5.isEmpty() || dis.equals("nil") || ar.equals("nil"))
        {
            Toast.makeText(getApplicationContext(), "Provide all the information to register", Toast.LENGTH_LONG).show();
        }
        else if(s5.length() != 11)
        {
            Toast.makeText(getApplicationContext(), "Wrong phone number length", Toast.LENGTH_LONG).show();
        }
        else if(!(temp.equals("013") || temp.equals("014") || temp.equals("015") || temp.equals("016") || temp.equals("017") || temp.equals("018") || temp.equals("019")))
        {
            Toast.makeText(getApplicationContext(), "Invalid phone number", Toast.LENGTH_LONG).show();
        }
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(s3).matches())
        {
            Toast.makeText(getApplicationContext(), "Email Id is invalid", Toast.LENGTH_LONG).show();
        }
        else {

            p.setMessage("Registering Riser");
            p.show();

            StringRequest s4 = new StringRequest(Request.Method.POST, constants.crider_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    p.dismiss();

                    try {
                        JSONObject j = new JSONObject(response);
                        if(!j.getBoolean("estat"))
                        {
                            Toast.makeText(getApplicationContext(), j.getString("info"), Toast.LENGTH_LONG).show();
                            spin_district.setSelection(0);
                            spin_area.setSelection(0);
                            dis = "nil";
                            ar = "nil";
                            t1.getText().clear();
                            t2.getText().clear();
                            t3.getText().clear();
                            t4.getText().clear();
                            t5.getText().clear();
                            startActivity(new Intent(getApplicationContext(),riderLogin.class));
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
                    m.put("phone",s5);
                    m.put("district",dis);
                    m.put("area",ar);
                    m.put("NID",s6);
                    m.put("password", s2);
                    return m;
                }
            };

            RequestHandler.getInstance(this).addToRequestQueue(s4);
        }
    }
}
