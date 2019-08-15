package com.example.foodie_buddy;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class sharedPrefManager {
    private static sharedPrefManager instance;
    private static Context ctx;

    private static final String MyPREFERENCES = "pref" ;
    private static final String Uid = "user_id";
    private static final String Name = "uname";
    private static final String Email = "umail";
    private static final String rests = "Restaurants";
    private static final String clickedtype = "clickType";
    private static final String curRestaurant = "curRest";
    private static final String curRestaurantId = "resId";
    private static final String curRestaurantfoods = "cusRestFoods";
    private static final String lat = "latitude";
    private static final String lng = "longitude";

    private sharedPrefManager(Context context) {
        ctx = context;
    }

    //make it static to get direct access from the class and then use the singleton pattern
    public static synchronized sharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new sharedPrefManager(context);
        }
        return instance;
    }

    public boolean userLogin(String response)
    {
        String name = null, eid=null;
        int id = -1;
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        SharedPreferences.Editor e = s.edit();
        try
        {
            JSONObject j = new JSONObject(response);
            JSONArray user = j.getJSONArray("record");
            //String name, eid;
            for(int i=0; i < user.length(); i++) {
                JSONObject k = user.getJSONObject(i);
                name = k.getString("name");
                eid = k.getString("email_id");
                id = k.getInt("id");
            }

        }
        catch (JSONException x) {
        }

        e.putInt(Uid,id);
        e.putString(Name,name);
        e.putString(Email,eid);
        e.apply();

        return true;
    }

    public void saveLocation(double lati, double longi)
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        SharedPreferences.Editor e = s.edit();
        e.putString(lat,Double.toString(lati));
        e.putString(lng,Double.toString(longi));
        e.apply();
    }

    public Double getLongitude()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        String tem = s.getString(lng,"99.9999");
        return Double.parseDouble(tem);
    }

    public Double getLatitude()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        String tem = s.getString(lat,"200.9999");
        return Double.parseDouble(tem);
    }

    public void saveRestaurants(String r)
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        SharedPreferences.Editor e = s.edit();
        e.putString(rests,r);
        e.apply();
    }

    public void saveClickedRestaurantType(String r)
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        SharedPreferences.Editor e = s.edit();
        e.putString(clickedtype,r);
        e.apply();
    }

    public void saveCurrentRestaurant(String r,int id)
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        SharedPreferences.Editor e = s.edit();
        e.putString(curRestaurant,r);
        e.putInt(curRestaurantId,id);
        e.apply();
    }

    public void saveCurrentRestFoods(String r)
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        SharedPreferences.Editor e = s.edit();
        e.putString(curRestaurantfoods,r);
        e.apply();
    }

    public boolean isLoggedIn()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        if(s.getString(Email,null) != null)//a user with the given key(Email id) is logged in
        {
            return true;
        }

        return false;
    }

    public String getRestaurants()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        return s.getString(rests,null);
    }

    public String getClickedType()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        return s.getString(clickedtype,null);
    }

    public String getCurrentRestaurant()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        return s.getString(curRestaurant,null);
    }

    public int getCurrentRestaurantId()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        return s.getInt(curRestaurantId,-1);
    }

    public String getCurrentRestaurantFoods()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        return s.getString(curRestaurantfoods,null);
    }

    public String getUserName()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        return s.getString(Name,null);
    }

    public int getUserId()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        return s.getInt(Uid,-1);
    }

    public boolean logout()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        SharedPreferences.Editor e = s.edit();
        e.clear();
        e.apply();
        return true;
    }
}
