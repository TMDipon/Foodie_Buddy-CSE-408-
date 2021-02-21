package com.example.foodie_buddy;

import java.util.HashMap;
import java.util.Map;

public class validateRegistration {

    public static String [] validateRegFields(Map<String,String> req)
    {
        String [] status = new String[2];

        //checking if any textfield is not given
        for(Map.Entry<String,String> obj:req.entrySet())
        {
            if(obj.getValue().isEmpty())
            {
                status[0] = "0";
                status[1] = "Provide necessary informations to register";
                return status;
            }
        }

        //check for district and area for rider
        if(req.containsKey("district"))
        {
            String temp = req.get("district");
            if(temp.equals("nil"))
            {
                status[0] = "0";
                status[1] = "Provide necessary informations to register";
                return status;
            }
        }

        //area
        if(req.containsKey("area"))
        {
            String temp = req.get("area");
            if(temp.equals("nil"))
            {
                status[0] = "0";
                status[1] = "Provide necessary informations to register";
                return status;
            }
        }

        //check for correct email pattern
        if(req.containsKey("email"))
        {
            String temp = req.get("email");
            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(temp).matches())
            {
                status[0] = "0";
                status[1] = "Invalid Email Id";
                return status;
            }
        }

        //check for correct phone number
        if(req.containsKey("phone"))
        {
            String temp = req.get("phone");
            if(temp.length() != 11)
            {
                status[0] = "0";
                status[1] = "Incorrect Phone number length";
                return status;
            }

            String tem = temp.substring(0,3);
            if(!(tem.equals("013") || tem.equals("014") || tem.equals("015") || tem.equals("016") || tem.equals("017") || tem.equals("018") || tem.equals("019")))
            {
                status[0] = "0";
                status[1] = "Invalid phone number";
                return status;
            }
        }

        status[0] = "1";
        status[1] = "All okay";

        return status;
    }
}
