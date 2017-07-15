package it.unibo.matteo.jappo.Model;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by matteo.minardi on 14/07/2017.
 */

public class User {

    private String name;
    private String email;
    private String imageName;

    private ArrayList<Item> favorites;

    public User(String name, String email, String imageName) {
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


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
