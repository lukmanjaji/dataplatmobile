/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jajitech.cn1.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jaji
 */
public class Helper {
    
    public String[] split(String tosplit, String pattern)
    {
        ArrayList<String> li = new ArrayList<String>();
        int len = pattern.length();
        boolean con = true;
        int ind;
        while(con)
        {
           ind = tosplit.indexOf(pattern);
           if(ind>0)
           {
               li.add(tosplit.substring(0, ind));
               
               tosplit = tosplit.substring(ind+len);
           }
           else
           {
              li.add(tosplit);
              con = false;
           }
        }
        
        
        String ret[] = new String[li.size()];
        return li.toArray(ret);   
        
    }
    
    
    public String toInt(String c)
    {
        String monthString="";
        switch (c) {
            case "Jan":  monthString = "01";
                     break;
            case "Feb":  monthString = "02";
                     break;
            case "Mar":  monthString = "03";
                     break;
            case "Apr":  monthString = "04";
                     break;
            case "May":  monthString = "05";
                     break;
            case "Jun":  monthString = "06";
                     break;
            case "Jul":  monthString = "07";
                     break;
            case "Aug":  monthString = "08";
                     break;
            case "Sep":  monthString = "09";
                     break;
            case "Oct": monthString = "10";
                     break;
            case "Nov": monthString = "11";
                     break;
            case "Dec": monthString = "12";
                     break;
        }
        return monthString;
    }
    
    public Map parse(String dateString)
    {
        Map m = new HashMap();
        String g[] = split(dateString, " ");
        //Tue Mar 13 16:11:32 WAT 2018
        m.put("day", g[2]);
        m.put("year", g[5]);
        m.put("month", toInt(g[1]));
        return m;
    }
    
    
    
}
