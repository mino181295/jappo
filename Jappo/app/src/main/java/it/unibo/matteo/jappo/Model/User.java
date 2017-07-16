package it.unibo.matteo.jappo.Model;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by matteo.minardi on 14/07/2017.
 */

public class User {

    private int id;
    private String name;
    private String email;
    private String imageName;

    private ArrayList<Item> favorites;

    public User(int id, String name, String email, String imageName) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.imageName = imageName;
    }

    public String getJson(){
        return new Gson().toJson(this);
    }

    public static User fromJson(String in){
        return new Gson().fromJson(in, User.class);
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getImageName() {
        return imageName;
    }

    public ArrayList<Item> getFavorites() {
        return favorites;
    }

    public void setFavorites(ArrayList<Item> favorites) {
        this.favorites = favorites;
    }
}
