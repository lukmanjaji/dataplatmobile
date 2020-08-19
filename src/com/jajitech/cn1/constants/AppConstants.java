/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jajitech.cn1.constants;

import com.codename1.io.Storage;

/**
 *
 * @author Jaji
 */
public class AppConstants {
   
   public  String URL = "";
   public  String AMAZON_URL = "";
  
   public AppConstants()
   {
       try
       {
        AMAZON_URL =  Storage.getInstance().readObject("LS").toString();
        URL = Storage.getInstance().readObject("FS").toString();
       }
       catch(Exception er)
       {
           AMAZON_URL = "<not set>";
           URL = "<not set>";
       }
       System.out.println(URL+"\n"+AMAZON_URL+"\n");
   }
    
    
}
