package com.example.foodie_buddy;

import java.io.Serializable;
import java.util.ArrayList;

public class orderManager implements Serializable
{
    private static ArrayList<Order> Orders = new ArrayList<Order>();

    public static void Add(Order o)
    {
        Orders.add(o);
    }

    public static void Remove(int i)
    {
        Orders.remove(i);
    }

    public static Order getOrder(int i)
    {
        return Orders.get(i);
    }

    public static int getLength()
    {
        return Orders.size();
    }

    public static int getOrderIndex(int id)
    {
        int idx = 0;
        for(int i=0;i<Orders.size();i++)
        {
            if(Orders.get(i).getOrderId() == id)
            {
                idx = i;
                break;
            }
        }
        return idx;
    }

    public static ArrayList<Order> getCurrentOrders()
    {
        return Orders;
    }

}
