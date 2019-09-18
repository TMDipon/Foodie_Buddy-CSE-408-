package com.example.foodie_buddy;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class constants {

    //private static final String ROOT_URL = "http://192.168.43.28/foodie_buddy/public/api/";
    private static  final String ROOT_URL = "http://10.0.2.2:8000/api/";
    public static final String cuser_URL = ROOT_URL+"createUser";
    public static final String luser_URL = ROOT_URL+"loginUser";
    public static final String cowner_URL = ROOT_URL+"createOwner";
    public static final String mrestowner_URL = ROOT_URL+"modifyRestaurant";
    public static final String modfoodshow_URL = ROOT_URL+"delshowfood";
    public static final String deleteFood_URL = ROOT_URL+"deleteFood";
    public static final String changeFoodPrice_URL = ROOT_URL+"changeFoodPrice";
    public static final String changeFoodDescription_URL = ROOT_URL+"changeFoodDescription";
    public static final String showres_URL = ROOT_URL+"restaurants";
    public static final String lowner_URL = ROOT_URL+"loginOwner";
    public static final String cres_URL = ROOT_URL+"createRestaurant";
    public static final String showfoods_URL = ROOT_URL+"foodByRestaurant";
    public static final String addFood_URL = ROOT_URL+"insertFood";
    public static final String updateOrderStatusonCancel_URL = ROOT_URL+"updateOrderStatusonCancellation";
    public static final String updateOrderStatusonReorder_URL = ROOT_URL+"updateOrderStatusonReorder";

    public static final String placeOrder_URL = ROOT_URL+"placeOrder";
    public static final String getOrderStatus_URL = ROOT_URL+"getOrderStatus";

    public static final String crider_URL = ROOT_URL+"createRider";
    public static final String lrider_URL = ROOT_URL+"loginRider";
    public static final String searchOrder_URL = ROOT_URL+"searchOrders";
    public static final String acceptOrder_URL = ROOT_URL+"acceptOrder";
    public static final String rejectOrder_URL = ROOT_URL+"rejectOrder";
    public static final String updateOrderStatus_URL = ROOT_URL+"updateOrderStatus";

    public static HashMap<String, String[]> mp;
    public static HashMap<String, String[]> areas;
    public static HashMap<String, String[]> districts;

    public static void make()
    {
        mp = new HashMap<>();
        String[] indian = {"Type","Set Menu","Chicken","Beef","Mutton","Tandoori","BBQ","Fish","Vegetables","Naan and Paratha","Biriyani","Polao","Dosa","Salad","Dessert","Drinks"};
        String[] burger = {"Type","Classic Burger","Junior Burger","Chef's Special","Gourmet Burger","Platter","Poutine","Mocktail","Shakes","Sides"};
        String[] all = {"Type","Appetizer","Soup","Shwarma","Pizza Sandwitch","Sandwitch","Pizza","Pasta","Set Menu","Noodles","Burger","Tandoori","BBQ","Kabab","Naan and Paratha","Chicken","Biriyani","Beef","Shakes","Dessert","Drinks"};
        String[] pizza_pasta = {"Type","Sides","Burger","Pasta","Pizza","Noodles and Chowmein","Set Menu","Drinks"};

        mp.put("Indian",indian);
        mp.put("Burger",burger);
        mp.put("All",all);
        mp.put("Pasta and Pizza",pizza_pasta);
        mp.put("Pizza",pizza_pasta);
        mp.put("Pasta",pizza_pasta);

        areas = new HashMap<>();
        String[] dhaka = {"Area","Banani","Banasree","Badda","Dhaka Cantonment","Dhanmondi","Gulshan","Kakrail","Khilgaon","Mirpur","Mohammadpur","Motijheel","New Paltan","Old Paltan","Tejgaon","Uttara"};
        String[] ar = {"Area"};

        areas.put("District",ar);
        areas.put("Dhaka",dhaka);

        districts = new HashMap<>();
        String[] bd = {"District","Dhaka"};
        districts.put("Bangladesh",bd);
    }


}
