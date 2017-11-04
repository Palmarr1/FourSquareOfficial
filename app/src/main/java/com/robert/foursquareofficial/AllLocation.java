package com.robert.foursquareofficial;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Robert on 10/23/2017.
 */

public class AllLocation implements Comparable<AllLocation>{

    public String dateTime;
    public List<IndividualLocation> individual = new ArrayList();
    public boolean locationDetermined;
    public IndividualLocation finalLocation;
    public String longitude;
    public String latitude;
    public String id;
    public String comment = "";
    public int rating = -1;
    public boolean booleanSearch = false;
    public SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");


    public AllLocation(){}
    public void setDateTime(String dT){
        dateTime = dT;
    }
    public AllLocation(String dT){dateTime = dT;}

    public boolean getLocationDetermined(){
        return locationDetermined;
    }
    public String setLocation(String id){
        for(IndividualLocation i : individual){

            if(i.getID().equals(id)){
                finalLocation = i;
                locationDetermined = true;
                return "Location Set";
            }
        }
        return "Location Not Avaialble";
    }

    public String getLocation(){
        if(locationDetermined == true){
            return finalLocation.getName();
        }else{
            return individual.get(0).getName();
        }
    }

    public String getDate(){
        return dateTime;
    }
    public void addLocation(IndividualLocation i){
        individual.add(i);
    }

    public List<IndividualLocation> returnList(){
        return individual;
    }
    public ArrayList returnArrayList(){
        ArrayList<String> aL = new ArrayList<>();
        for(IndividualLocation i : individual){
            aL.add(i.getName());
        }
        return aL;
    }
    public void selectfinal(int i){
        finalLocation = individual.get(i);
        locationDetermined = true;
    }


    @Override
    public int compareTo(@NonNull AllLocation o) {
        try {
            Date date1 = dateFormat.parse(o.dateTime);
            Date date2 = dateFormat.parse(this.dateTime);

            return date1.compareTo(date2);
        }catch(Exception e){e.printStackTrace();}

        return 0;

    }
}