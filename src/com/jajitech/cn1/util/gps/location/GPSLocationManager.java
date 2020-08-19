/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jajitech.cn1.util.gps.location;

import com.codename1.location.Location;
import com.codename1.location.LocationListener;
import com.codename1.location.LocationManager;

/**
 *
 * @author Usman
 */
public class GPSLocationManager {
    
    private double lat, lon;
    LocationManager l;

public double getLat(){
    return lat;
}

public double getLon(){
    return lon;
}


public void doLocation()
{
    
}


public void getLoc(){
try {
    int i = 0;
    l = LocationManager.getLocationManager();
    Location loc = l.getCurrentLocation();
    if(loc.getStatus()==LocationManager.AVAILABLE){
        lat = loc.getLatitude();
        lon = loc.getLongitude();
    }

    l.setLocationListener(new LocationListener() {

        public void locationUpdated(Location location) {
            //positionMethod(location);
        }

        public void providerStateChanged(int newState) {
            //positionMethod();
        }
    });

    int ii = 0;
    while (i == 0 && ii < 3) {
        Thread.sleep(10000);
        ii++;
    }
} catch (Exception ex) {
    lat = 0.0;
    lon = 0.0;
}
}
    
}
