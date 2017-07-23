package it.unibo.matteo.jappo.Model;

import com.google.gson.Gson;

import java.util.ArrayList;


public class Order {

    ArrayList<Item> ordered;
    ArrayList<Item> arrived;

    Restaurant restourant;

    public Order(Restaurant r) {
        this.ordered = new ArrayList<>();
        this.arrived = new ArrayList<>();
        this.restourant = r;
    }

    public Restaurant getRestaurant() {
        return restourant;
    }

    public void setArrivedItem(Item i){
        if (ordered.contains(i)){
            ordered.remove(i);
            arrived.add(i);
            i.setTime(Item.getCurrentTime());
        }
    }

    public ArrayList<Item> getOrdered() {
        return ordered;
    }

    public ArrayList<Item> getArrived() {
        return arrived;
    }

    public String getJson(){
        return new Gson().toJson(this);
    }

    public static Order fromJson(String in){
        return new Gson().fromJson(in, Order.class);
    }

}
