package com.robert.foursquareofficial;

/**
 * Created by Robert on 10/23/2017.
 */

public class IndividualLocation{

    public String id;
    public String name;

    public IndividualLocation(String i, String n){
        id = i;
        name = n;
    }
    public IndividualLocation(){}

    public String getID(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String toString(){
        return id + " : " + name;
    }
}