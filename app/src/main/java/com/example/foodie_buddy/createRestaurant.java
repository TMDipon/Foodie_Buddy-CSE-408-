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

public class createRestaurant extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String typ="nil";
    private String st="nil";
    private String et="nil";
    private String dis="nil";
    private String ar="nil";

    public Spinner spin_type,spin_stime,spin_etime,spin_district,spin_area;
    public EditText t1,t2,t3,t4,t5,t6;
    public String type;
    public int rid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);

        spin_type = findViewById(R.id.type);//spinner for restaurant types
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_type.setAdapter(adapter);
        spin_type.setOnItemSelectedListener(this);


        spin_stime = findViewById(R.id.stime);//spinner for starting time
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.start_time, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_stime.setAdapter(adapter1);
        spin_stime.setOnItemSelectedListener(this);

        spin_etime = findViewById(R.id.etime);//spinner for closing time
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.end_time, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_etime.setAdapter(adapter2);
        spin_etime.setOnItemSelectedListener(this);

        spin_district = findViewById(R.id.district);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.district, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_district.setAdapter(adapter3);
        spin_district.setOnItemSelectedListener(this);

        spin_area = findViewById(R.id.area);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this,
                R.array.areas, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_area.setAdapter(adapter4);
        spin_area.setOnItemSelectedListener(this);
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
            case R.id.type :
                if(!adapterView.getItemAtPosition(i).toString().equals("Type"))
                {
                    typ = adapterView.getItemAtPosition(i).toString();
                    Toast.makeText(getApplicationContext(), typ, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.stime :
                if(!adapterView.getItemAtPosition(i).toString().equals("Opens at"))
                {
                    st = adapterView.getItemAtPosition(i).toString();
                    Toast.makeText(getApplicationContext(), st, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.etime :
                if(!adapterView.getItemAtPosition(i).toString().equals("Closes at"))
                {
                    et = adapterView.getItemAtPosition(i).toString();
                    Toast.makeText(getApplicationContext(), et, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.district :
                if(!adapterView.getItemAtPosition(i).toString().equals("District"))
                {
                    dis = adapterView.getItemAtPosition(i).toString();
                    Toast.makeText(getApplicationContext(), dis, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.area :
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

    public void createRes(View v)
    {
        t1 = (EditText)findViewById(R.id.rname);
        t2 = (EditText)findViewById(R.id.rdno);
        t3 = (EditText)findViewById(R.id.rdname);
        t4 = (EditText)findViewById(R.id.hsno);
        t5 = (EditText)findViewById(R.id.hsname);
        t6 = (EditText)findViewById(R.id.level);

        final String s1,s2,s3,s4,s5,s6;
        if(t3.getText().toString().trim().isEmpty())
        {
            s3 = "null";
        }
        else
        {
            s3 = t3.getText().toString();
        }

        if(t5.getText().toString().trim().isEmpty())
        {
            s5 = "null";
        }
        else
        {
            s5 = t5.getText().toString();
        }

        s1 = t1.getText().toString();
        s2 = t2.getText().toString();
        s4 = t4.getText().toString();
        s6 = t6.getText().toString();

        final String tem = Integer.toString(sharedOwnerManager.getInstance(this).getOwnerId());

        final ProgressDialog p = new ProgressDialog(this);

        if(s1.isEmpty() || s2.isEmpty() || s4.isEmpty() || s6.isEmpty() || typ.equals("nil") || st.equals("nil") || et.equals("nil") || dis.equals("nil") || ar.equals("nil"))
        {
            Toast.makeText(getApplicationContext(), "Provide or select necessary information for creating restaurant", Toast.LENGTH_LONG).show();
        }
        else {

            p.setMessage("Registering, Please Wait....");
            p.show();

            StringRequest s7 = new StringRequest(Request.Method.POST, constants.cres_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    p.dismiss();


                    try {
                        JSONObject j = new JSONObject(response);
                        if(!j.getBoolean("estat"))
                        {
                            JSONArray user = j.getJSONArray("record");
                            //String name, eid;
                            for(int i=0; i < user.length(); i++) {
                                JSONObject k = user.getJSONObject(i);
                                rid = k.getInt("id");
                                type = k.getString("type");
                            }
                            sharedOwnerManager.getInstance(getApplicationContext()).current_Rest(rid,type);
                            Toast.makeText(getApplicationContext(), j.getString("info")+sharedOwnerManager.getInstance(getApplicationContext()).getCurrentResType(), Toast.LENGTH_SHORT).show();
                            spin_type.setSelection(0);
                            spin_stime.setSelection(0);
                            spin_etime.setSelection(0);
                            spin_district.setSelection(0);
                            spin_area.setSelection(0);
                            typ = "nil";
                            st = "nil";
                            et = "nil";
                            dis = "nil";
                            ar = "nil";
                            t1.getText().clear();
                            t2.getText().clear();
                            t3.getText().clear();
                            t4.getText().clear();
                            t5.getText().clear();
                            t6.getText().clear();
                            startActivity(new Intent(getApplicationContext(),FoodInsert.class));
                        }
                        else
                        {
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
                    m.put("name", s1);
                    m.put("type", typ);
                    m.put("owner_id", tem);
                    m.put("starts_at", st);
                    m.put("closes_at",et);
                    m.put("district",dis);
                    m.put("area",ar);
                    m.put("Road_name",s3);
                    m.put("Road_no",s2);
                    m.put("House_name",s5);
                    m.put("House_no",s4);
                    m.put("Level",s6);
                    return m;
                }
            };

            RequestHandler.getInstance(this).addToRequestQueue(s7);
        }
    }


}
