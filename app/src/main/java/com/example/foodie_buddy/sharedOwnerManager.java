package com.example.foodie_buddy;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class sharedOwnerManager {
    private static sharedOwnerManager instance;
    private static Context ctx;

    private static final String MyPREFERENCES = "owner" ;
    private static final String Name = "oname";
    private static final String Email = "omail";
    private static final String ID = "identifier";
    private static final String rests = "restaurantsUnder";
    //Below will be the properties of the clicked restaurant
    private static final String rname = "restName";
    private static final String rid = "restId";
    private static final String Type = "Type";

    private static final String curRestFoods = "curRFoods";

    private sharedOwnerManager(Context context) {
        ctx = context;
    }

    public static synchronized sharedOwnerManager getInstance(Context context) {
        if (instance == null) {
            instance = new sharedOwnerManager(context);
        }
        return instance;
    }

    public boolean ownerLogin(int id,String name,String email)
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        SharedPreferences.Editor e = s.edit();

        e.putInt(ID, id);
        e.putString(Name,name);
        e.putString(Email,email);
        e.apply();

        return true;
    }

    public boolean current_Rest(int id,String type)
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        SharedPreferences.Editor e = s.edit();

        e.putInt(rid,id);
        e.putString(Type,type);
        e.apply();

        return true;
    }

    public boolean isOwnerLoggedIn()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        if(s.getString(Email,null) == null)//a user with the given key(Email id) is logged in
        {
            return false;
        }

        return true;
    }

    public int getOwnerId()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        return s.getInt(ID,-1);
    }

    public String getOwnerName()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        return s.getString(Name,null);
    }

    public String getOwnerEmail()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        return s.getString(Email,null);
    }

    public String getCurrentResType()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        return s.getString(Type,null);
    }

    public int getCurrentResId()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        return s.getInt(rid,-1);
    }

    public void saveRestaurantsUnder(String st)
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        SharedPreferences.Editor e = s.edit();

        e.putString(rests,st);
        e.apply();
    }

    public String getRestaurantsUnder()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        return s.getString(rests,null);
    }

    public void saveCurRestFoods(String st)
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        SharedPreferences.Editor e = s.edit();
        e.putString(curRestFoods,st);
        e.apply();
    }

    public String getFoodsUnder()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        return s.getString(curRestFoods,null);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void deleteFoodinCache(int id)
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        SharedPreferences.Editor e = s.edit();
        try
        {
            JSONArray J =new JSONArray(s.getString(curRestFoods,null));
            for(int i=0;i<J.length();i++)
            {
                JSONObject k = J.getJSONObject(i);
                if(k.getInt("id") == id)
                {
                    J.remove(i);
                    break;
                }
            }

            String tmp = J.toString();
            e.putString(curRestFoods,tmp);
            e.apply();

        }
        catch(JSONException x){}
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void changeFoodPriceinCache(int id,double price)
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        SharedPreferences.Editor e = s.edit();
        try
        {
            JSONArray J =new JSONArray(s.getString(curRestFoods,null));
            for(int i=0;i<J.length();i++)
            {
                JSONObject k = J.getJSONObject(i);
                if(k.getInt("id") == id)
                {
                    k.put("unit_price",price);
                    break;
                }
            }

            String tmp = J.toString();
            e.putString(curRestFoods,tmp);
            e.apply();
        }
        catch(JSONException x){}
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
