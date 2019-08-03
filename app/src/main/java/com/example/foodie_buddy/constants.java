package com.example.foodie_buddy;

import java.util.HashMap;
import java.util.Map;

public class constants {
    //private static final String ROOT_URL = "http://192.168.0.113/Test_F_Buddy/web_services/";
    private static  final String ROOT_URL = "http://10.0.2.2:8000/api/";
    public static final String cuser_URL = ROOT_URL+"createUser";
    public static final String luser_URL = ROOT_URL+"loginUser";
    public static final String cowner_URL = ROOT_URL+"createOwner";
    public static final String mrestowner_URL = ROOT_URL+"modifyRestaurant";
    public static final String modfoodshow_URL = ROOT_URL+"delshowfood";
    public static final String deleteFood_URL = ROOT_URL+"deleteFood";
    public static final String changeFoodPrice_URL = ROOT_URL+"changeFoodPrice";
    public static final String showres_URL = ROOT_URL+"restaurants";
    public static final String lowner_URL = ROOT_URL+"loginOwner";
    public static final String cres_URL = ROOT_URL+"createRestaurant";
    public static final String showfoods_URL = ROOT_URL+"foodByRestaurant";
    public static final String addFood_URL = ROOT_URL+"insertFood";

    public static HashMap<String, String[]> mp;

    public static void make()
    {
        mp = new HashMap<>();
        String[] indian = {"Type","Set Menu","Chicken","Beef","Mutton","Tandoori","BBQ","Fish","Vegetables","Naan and Paratha","Biriyani","Polao","Dosa","Salad","Dessert","Drinks"};
        String[] burger = {"Type","Classic Burger","Junior Burger","Chef's Special","Gourmet Burger","Platter","Poutine","Mocktail","Shakes","Sides"};
        String[] all = {"Type","Appetizer","Soup","Shwarma","Pizza Sandwitch","Sandwitch","Pizza","Pasta","Set Menu","Noodles","Burger","Tandoori","BBQ","Kabab","Naan and Paratha","Chicken","Biriyani","Beef","Shakes","Dessert","Drinks"};

        mp.put("Indian",indian);
        mp.put("Burger",burger);
        mp.put("All",all);
    }



}
