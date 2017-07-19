package it.unibo.matteo.jappo.Model;

import com.google.gson.Gson;

import java.util.ArrayList;


public class User {

    private int id;
    private String name;
    private String email;

    private ArrayList<Item> favorites;

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
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

    public ArrayList<Item> getFavorites() {
        return favorites;
    }

    public void setFavorites(ArrayList<Item> favorites) {
        this.favorites = favorites;
    }

    public String getProfileImage(){
        return "profile_" + getId() + ".jpg";
    }


}
