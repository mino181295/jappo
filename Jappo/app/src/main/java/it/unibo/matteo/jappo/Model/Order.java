package it.unibo.matteo.jappo.Model;

import java.util.ArrayList;


public class Order {

    ArrayList<Item> orderedItems;
    ArrayList<Item> arrivedItems;

    Restaurant restourant;

    public Order(ArrayList<Item> items){
        this.orderedItems = items;
        this.arrivedItems = new ArrayList<>();
    }

    public Order(){
        this(new ArrayList<Item>());
    }

    public Restaurant getRestourant() {
        return restourant;
    }

    public void setRestourant(Restaurant restourant) {
        this.restourant = restourant;
    }

    public void addItem(Item i){
        this.orderedItems.add(i);
    }

    public void arrivedItem(Item i){
        if (orderedItems.contains(i)){
            orderedItems.remove(i);
            arrivedItems.add(i);
        }
    }

}
