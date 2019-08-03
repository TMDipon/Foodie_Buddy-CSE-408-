package com.example.foodie_buddy;

public class FoodItem {
    private int id;
    private String name;
    private String type;
    private String description;
    private double unit_price;

    public FoodItem(int x,String a,String b,String c,double d)
    {
        id = x;
        name = a;
        type = b;
        description = c;
        unit_price = d;
    }

    public String formatFood()
    {
        if(this.description.equalsIgnoreCase("NA"))
        {
            return this.name+"\ntk: "+Double.toString(this.unit_price);
        }
        return this.name+"\n"+this.description+"\ntk: "+Double.toString(this.unit_price);
    }

    public int getFoodId(){ return this.id;}

    public String getFoodName(){ return this.name;}

    public double getFoodPrice(){ return this.unit_price;}

    public String getFoodDesc()
    {
        if(this.description.equalsIgnoreCase("NA"))
        {
            return "";
        }
        return this.description;
    }

}
