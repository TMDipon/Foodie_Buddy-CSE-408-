package com.example.foodie_buddy;

public class uRestaurant {
    private int id;
    private String name;
    private String type;

    public uRestaurant(String a,String b,int c)
    {
        name = a;
        type = b;
        id = c;
    }

    public String getName()
    {
        return this.name;
    }
    public String getType()
    {
        return this.type;
    }
    public int getId()
    {
        return this.id;
    }
}