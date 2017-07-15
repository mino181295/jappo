package it.unibo.matteo.jappo.Model;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;


public class Restaurant {

    private String name;
    private String address;
    private String city;

    private LatLng coordinates;

    public Restaurant(String name, String address, String city, Float latitude, Float longitude) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.coordinates = new LatLng(latitude, longitude);
    }

    public String getJson(){
        return new Gson().toJson(this);
    }

    public static Restaurant fromJson(String in){
        return new Gson().fromJson(in, Restaurant.class);
    }


}
