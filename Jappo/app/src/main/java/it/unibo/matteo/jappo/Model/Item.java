package it.unibo.matteo.jappo.Model;

import com.google.gson.Gson;

/**
 * Created by matteo.minardi on 14/07/2017.
 */

public class Item {

    private String name;
    private int number;
    private Type type;
    private Restaurant restaurant;

    public Item(String name, int number, Type type, Restaurant restaurant) {
        this.name = name;
        this.number = number;
        this.type = type;
        this.restaurant = restaurant;
    }

    public String getJson(){
        return new Gson().toJson(this);
    }

    public static Item fromJson(String in){
        return new Gson().fromJson(in, Item.class);
    }

}
