package it.unibo.matteo.jappo.Model;

import com.google.gson.Gson;

import java.util.ArrayList;


public class Order {

    ArrayList<Item> orderedItems;
    ArrayList<Item> arrivedItems;

    Restaurant restourant;

    public Order(Restaurant r) {
        this.orderedItems = new ArrayList<>();
        this.arrivedItems = new ArrayList<>();
        this.restourant = r;
    }

    public Order(){
        this(null);
    }

    public Restaurant getRestourant() {
        return restourant;
    }

    public void setRestourant(Restaurant restourant) {
        this.restourant = restourant;
    }

    public void addItem(Item i){
        orderedItems.add(i);
    }

    public void setArrivedItem(Item i){
        if (orderedItems.contains(i)){
            orderedItems.remove(i);
            arrivedItems.add(i);
            i.setTime(Item.getCurrentTime());
        }
    }

    public ArrayList<Item> getOrderedItems() {
        return orderedItems;
    }

    public ArrayList<Item> getArrivedItems() {
        return arrivedItems;
    }

    public String getJson(){
        return new Gson().toJson(this);
    }

    public static Order fromJson(String in){
        return new Gson().fromJson(in, Order.class);
    }

}
