package com.example.foodie_buddy;

import java.io.Serializable;

public class OrderItem implements Serializable {

    private int FoodId;
    private String FoodName;
    private int Amount;
    private double unitPrice;


    public OrderItem(int x, String name, double up)
    {
        FoodId = x;
        FoodName = name;
        Amount = 1;
        unitPrice = up;
    }

    public String getOrderItemDesc()
    {
        return Integer.toString(FoodId)+"_"+FoodName+"_"+Integer.toString(Amount)+"_"+Double.toString(unitPrice);
    }

    public void addItem()
    {
        this.Amount++;
    }

    public void removeItem()
    {
        this.Amount--;
    }

    public int getFoodId(){ return this.FoodId; }

    public int getAmount(){ return this.Amount; }

    public String getFoodName(){ return this.FoodName; }

    public double getFoodPrice(){ return this.unitPrice; }

}
