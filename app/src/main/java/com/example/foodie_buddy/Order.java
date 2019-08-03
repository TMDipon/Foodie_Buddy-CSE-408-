package com.example.foodie_buddy;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {
    private int RestaurantId;
    private int UserId;
    private String RestaurantName;
    private ArrayList<OrderItem> ordered_Items;

    public Order(int x, int y, String z)
    {
        UserId = x;
        RestaurantId = y;
        RestaurantName = z;
        ordered_Items = new ArrayList<OrderItem>();
    }

    public String getRestaurantName(){ return this.RestaurantName; }

    public ArrayList<OrderItem> getOrdered_Items(){ return ordered_Items; }

    public int getOrderStat()
    {
        if(!ordered_Items.isEmpty())
        {
            return ordered_Items.size();
        }
        return 0;
    }

    public int getOrderItemAmount(int foodId)
    {
        int i = findFood(foodId);
        if(i != -1)
        {
            return ordered_Items.get(i).getAmount();
        }
        return 0;
    }

    public int findFood(int x)
    {
        for(int i=0; i< ordered_Items.size();i++)
        {
            if(ordered_Items.get(i).getFoodId() == x)
            {
                return i;
            }
        }
        return -1;
    }

    public int Add(int foodId, String foodName, double price)
    {
        int stat = findFood(foodId);
        if(stat == -1)
        {
            OrderItem tem = new OrderItem(foodId, foodName, price);
            ordered_Items.add(tem);
        }
        else
        {
            ordered_Items.get(stat).addItem();
            return ordered_Items.get(stat).getAmount();
        }

        return 1;
    }

    public int Remove(int foodId)
    {
        int stat = findFood(foodId);
        int am = -1;
        if(stat != -1)      //means the food is added so can be removed
        {
            ordered_Items.get(stat).removeItem();
            am = ordered_Items.get(stat).getAmount();//see the amount of this item, if 0 then remove from the list

            if( am == 0)
            {
                ordered_Items.remove(stat);
                return 2;       //means the food is no longer in the cart
            }
            return 1;       //means some amount of this food is still in the cart
        }
        return 0;
    }
}
