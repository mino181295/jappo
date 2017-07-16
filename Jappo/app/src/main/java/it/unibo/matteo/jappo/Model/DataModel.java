package it.unibo.matteo.jappo.Model;

import android.content.Context;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import it.unibo.matteo.jappo.Utils.HTTPHelper;
import it.unibo.matteo.jappo.Utils.JSONHelper;
import it.unibo.matteo.jappo.Utils.RequestType;
import it.unibo.matteo.jappo.Utils.SharedPreferencesManager;

import static it.unibo.matteo.jappo.Fragment.FavoritesFragment.favorites;

public class DataModel {

    public static final String SP_NAME = "Default";

    User user;
    Order currentOrder;
    ArrayList<Restaurant> availableRestaurants;

    private static SharedPreferencesManager spManager;

    public DataModel(Context context){
        spManager = new SharedPreferencesManager(SP_NAME, context);
        if (spManager.isLoggedIn()){
            this.user = spManager.getLoggedUser();
        }
        loadData(this.user);
    }

    public static DataModel load(){
        return spManager.loadDataModel();
    }

    public void save(){
        spManager.writeDataModel(this);
    }

    private void loadData(User user){
        loadRestourants();
        loadFavorites(user);
        loadOrder(user);
    }

    private void loadFavorites(User u){
        HashMap<String, String> loadParams = new HashMap<>();
        loadParams.put(RequestType.getDefault(), RequestType.GET_FAV.getValue());
        loadParams.put("id", String.valueOf(u.getId()));

        String response = HTTPHelper.connectPost(HTTPHelper.REST_BACKEND, loadParams);
        ArrayList<Item> favorites = JSONHelper.parseFavorites(response);
        u.setFavorites(favorites);
    }

    private void loadOrder(User u){
    }

    private void loadRestourants(){
        HashMap<String, String> loadParams = new HashMap<>();
        loadParams.put(RequestType.getDefault(), RequestType.GET_REST.getValue());

        String response = HTTPHelper.connectPost(HTTPHelper.REST_BACKEND, loadParams);
        availableRestaurants = JSONHelper.parseRestaurants(response);
    }

    public User getLoggedUser(){
        return user;
    }

    public ArrayList<Restaurant> getRestaurants(){
        return this.availableRestaurants;
    }

    public static DataModel fromJson(String in){
        return new Gson().fromJson(in, DataModel.class);
    }

    public String getJson(){
        return new Gson().toJson(this);
    }

}
