package com.example.foodie_buddy;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class sharedRiderManager {

    private static sharedRiderManager instance;
    private static Context ctx;

    private static final String MyPREFERENCES = "pref" ;
    private static final String Rid = "rider_id";
    private static final String Rname = "rname";
    private static final String Remail = "rmail";
    private static final String Rarea = "rarea";
    private static final String Rdistrict = "rdistrict";
    private static final String Online_stat = "ostat";//0->offline and 1->online

    private sharedRiderManager(Context context) {
        ctx = context;
    }

    //make it static to get direct access from the class and then use the singleton pattern
    public static synchronized sharedRiderManager getInstance(Context context) {
        if (instance == null) {
            instance = new sharedRiderManager(context);
        }
        return instance;
    }

    public boolean riderLogin(String response)
    {
        String name = null, eid=null, area = null, district = null;
        int id = -1;
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        SharedPreferences.Editor e = s.edit();
        try
        {
            JSONObject j = new JSONObject(response);
            JSONArray rider = j.getJSONArray("record");
            for(int i=0; i < rider.length(); i++) {
                JSONObject k = rider.getJSONObject(i);
                name = k.getString("name");
                eid = k.getString("email_id");
                id = k.getInt("id");
                area = k.getString("area");
                district = k.getString("district");
            }

        }
        catch (JSONException x) {
        }

        e.putInt(Rid,id);
        e.putString(Rname,name);
        e.putString(Remail,eid);
        e.putString(Rarea,area);
        e.putString(Rdistrict,district);
        e.putInt(Online_stat,0);
        e.apply();

        return true;
    }

    public boolean isRiderLoggedIn()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        if(s.getString(Remail,null) != null)//a rider with the given key(Email id) is logged in
        {
            return true;
        }

        return false;
    }
}
