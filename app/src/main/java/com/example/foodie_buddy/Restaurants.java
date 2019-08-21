package com.example.foodie_buddy;

public class Restaurants {

    private int id;
    private String name;
    private String type;
    private String opens_at;
    private String closes_at;

    public Restaurants(int x,String a,String b,String c,String d)
    {
        id = x;
        name = a;
        type = b;
        opens_at = c;
        closes_at = d;
    }

    public String format()
    {
        return this.name+"\nCuisine type: "+this.type+"\nOpens at: "+this.opens_at+"            Closes at: "+this.closes_at;
    }

    public int getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public String getType()
    {
        return this.type;
    }
}