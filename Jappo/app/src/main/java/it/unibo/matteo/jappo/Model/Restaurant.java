package it.unibo.matteo.jappo.Model;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;


public class Restaurant {

    private String name;
    private String city;
    private String address;

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

    @Override
    public String toString() {
        return this.name + ", " + this.city;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
