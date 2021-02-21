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

        final HashMap<String, String> map = new HashMap<>();

        map.put("name",t1.getText().toString().trim());
        map.put("password",t2.getText().toString().trim());
        map.put("email",t3.getText().toString().trim());
        map.put("phone",t4.getText().toString().trim());
        map.put("nid",t5.getText().toString().trim());
        map.put("district",dis);
        map.put("area",ar);

        final ProgressDialog p = new ProgressDialog(this);

        String [] status = validateRegistration.validateRegFields(map);

        if(status[0].equals("0"))
        {
            Toast.makeText(getApplicationContext(), status[1], Toast.LENGTH_LONG).show();
        }
        else {

            p.setMessage("Registering Rider");
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
                    m.put("name", map.get("name"));
                    m.put("email_id", map.get("email"));
                    m.put("phone",map.get("phone"));
                    m.put("district",map.get("district"));
                    m.put("area",map.get("area"));
                    m.put("NID",map.get("nid"));
                    m.put("password", map.get("password"));
                    return m;
                }
            };

            RequestHandler.getInstance(this).addToRequestQueue(s4);
        }
    }
}
