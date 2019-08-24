package com.example.foodie_buddy;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
    private static final String rider_init_lat = "rilat";
    private static final String rider_init_longi = "rilon";
    private static final String rider_cur_lat = "rclat";
    private static final String rider_cur_longi = "rclon";

    private static final String order_id = "oid";
    private static final String user_lat = "ulat";
    private static final String user_longi = "ulon";
    private static final String res_name = "rname";
    private static final String res_lat = "rlat";
    private static final String res_longi = "rlon";
    private static final String res_address = "raddress";
    private static final String res_phone = "rphone";
    private static final String user_name = "uname";
    private static final String user_phone = "uphone";
    private static final String item_desc = "iDesc";


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

    public String getAddress(String a, String b, String c, String d, String e, String f, String g) {
        if (e.equalsIgnoreCase("null")) {
            e = "";
        } else {
            e += ", ";
        }
        if (c.equalsIgnoreCase("null")) {
            c = "";
        } else {
            c += ", ";
        }

        return "Level " + g + ", " + f + ", " + e + d + ", " + c + b + ", " + a;
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

    //save rider location and getting locations
    public void saveRiderInitLocation(double lati, double longi)
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        SharedPreferences.Editor e = s.edit();
        e.putString(rider_init_lat,Double.toString(lati));
        e.putString(rider_init_longi,Double.toString(longi));
        e.apply();
    }

    public void saveRiderCurrentLocation(double lati, double longi)
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        SharedPreferences.Editor e = s.edit();
        e.putString(rider_cur_lat,Double.toString(lati));
        e.putString(rider_cur_longi,Double.toString(longi));
        e.apply();
    }

    public Double getriLongitude()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        String tem = s.getString(rider_init_longi,"99.9999");
        return Double.parseDouble(tem);
    }

    public Double getriLatitude()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        String tem = s.getString(rider_init_lat,"200.9999");
        return Double.parseDouble(tem);
    }

    public Double getrcLongitude()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        String tem = s.getString(rider_cur_longi,"99.9999");
        return Double.parseDouble(tem);
    }

    public Double getrcLatitude()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        String tem = s.getString(rider_cur_lat,"200.9999");
        return Double.parseDouble(tem);
    }

    public Double getuserLongitude()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        String tem = s.getString(user_longi,"99.9999");
        return Double.parseDouble(tem);
    }

    public Double getuserLatitude()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        String tem = s.getString(user_lat,"200.9999");
        return Double.parseDouble(tem);
    }

    public Double getrestLongitude()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        String tem = s.getString(res_longi,"99.9999");
        return Double.parseDouble(tem);
    }

    public Double getrestLatitude()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        String tem = s.getString(res_lat,"200.9999");
        return Double.parseDouble(tem);
    }
    //-------------------------------------------



    public void saveOrder(String response)
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        SharedPreferences.Editor e = s.edit();
        try
        {
            JSONObject j = new JSONObject(response);

            //Saving restaurant credentials and user location from order description odesc
            JSONObject odesc = j.getJSONObject("odesc");
            e.putInt(order_id,odesc.getInt("id"));
            e.putString(res_name,odesc.getString("name"));
            e.putString(user_lat,Double.toString(odesc.getDouble("user_lati")));
            e.putString(user_longi,Double.toString(odesc.getDouble("user_longi")));
            e.putString(res_lat,Double.toString(odesc.getDouble("restaurant_lati")));
            e.putString(res_longi,Double.toString(odesc.getDouble("restaurant_longi")));
            String addr = getAddress(odesc.getString("district"),odesc.getString("area"),odesc.getString("Road_name"),odesc.getString("Road_no"),odesc.getString("House_name"),odesc.getString("House_no"),odesc.getString("Level"));
            e.putString(res_address,addr);
            e.putString(res_phone,odesc.getString("phone"));

            //Saving user name and phone number of the user who has given the order
            JSONObject udesc = j.getJSONObject("udesc");
            e.putString(user_name,udesc.getString("name"));
            e.putString(user_phone,udesc.getString("phone"));

            //Saving the items inside order
            JSONArray idesc = j.getJSONArray("idesc");
            e.putString(item_desc,idesc.toString());

            e.apply();
        }
        catch (JSONException x) {
        }

    }

    public void setOnlineStatus(int x)
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        SharedPreferences.Editor e = s.edit();
        e.putInt(Online_stat,x);
        e.apply();
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

    public String getRiderName()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        return s.getString(Rname,null);
    }

    public int getRiderId()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        return s.getInt(Rid,-1);
    }

    public String getRiderPrefferedArea()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        return s.getString(Rarea,null);
    }

    public int getOnlineStat()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        return s.getInt(Online_stat,-1);
    }

    //Methods needed for order details showing ------------
    public String getRestName()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        return s.getString(res_name,null);
    }

    public String getRestAddress()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        return s.getString(res_address,null);
    }

    public String getOrderId()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        int  tem = s.getInt(order_id,-1);
        return Integer.toString(tem);
    }

    public String getRestPhone()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        return s.getString(res_phone,null);
    }

    public ArrayList<String> getOrderItems()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        String tem = s.getString(item_desc,null);
        ArrayList<String> l1 = new ArrayList<String>();
        String space = "       ";

        try
        {
            JSONArray items = new JSONArray(tem);
            for(int i=0; i < items.length(); i++) {
                JSONObject k = items.getJSONObject(i);
                Double price = k.getInt("amount")*k.getDouble("unit_price");
                l1.add(Integer.toString(k.getInt("amount"))+"* "+k.getString("name")+space+"Price: "+Double.toString(price));
            }

        }
        catch (JSONException x) {
        }

        return l1;
    }

    public double getTotalPrice()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        String tem = s.getString(item_desc,null);
        double totalPrice = 0.0;

        try
        {
            JSONArray items = new JSONArray(tem);
            for(int i=0; i < items.length(); i++) {
                JSONObject k = items.getJSONObject(i);
                Double price = k.getInt("amount")*k.getDouble("unit_price");
                totalPrice += price;
            }

        }
        catch (JSONException x) {
        }
        return totalPrice;
    }


    //For logging out
    public boolean riderlogout()
    {
        SharedPreferences s = ctx.getSharedPreferences(MyPREFERENCES,ctx.MODE_PRIVATE);
        SharedPreferences.Editor e = s.edit();
        e.clear();
        e.apply();
        return true;
    }
}
