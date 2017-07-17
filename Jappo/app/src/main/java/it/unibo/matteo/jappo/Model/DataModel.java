package it.unibo.matteo.jappo.Model;

import android.content.Context;
import android.util.Log;

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

    private User user;
    private Order currentOrder;
    private ArrayList<Restaurant> availableRestaurants;

    private static SharedPreferencesManager spManager;

    public DataModel(Context context){
        spManager = new SharedPreferencesManager(SP_NAME, context);

        user = null;
        currentOrder = null;
        availableRestaurants = null;
    }

    public void load(){
        if (spManager.isLoggedIn()){
            user = spManager.getLoggedUser();
            if (spManager.hasDataModel()) {
                DataModel dm = spManager.loadDataModel();
                user = dm.user;
                currentOrder = dm.currentOrder;
                availableRestaurants = dm.availableRestaurants;
            } else {
                loadData(user);
            }
        }
    }

    public void save(){
        spManager.writeDataModel(this);
    }

    public void remove(){
        spManager.clearDataModel();
    }

    public void login(User u){
        user = u;
        spManager.setLoggedUser(user);
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

    public void uploadFavorites(){
        HashMap<String, String> loadParams = new HashMap<>();
        loadParams.put(RequestType.getDefault(), RequestType.UPLOAD_FAV.getValue());
        loadParams.put("fav", new Gson().toJson(user.getFavorites()));
        loadParams.put("user", String.valueOf(user.getId()));

        String response = HTTPHelper.connectPost(HTTPHelper.REST_BACKEND, loadParams);
    }

    private void loadRestourants(){
        HashMap<String, String> loadParams = new HashMap<>();
        loadParams.put(RequestType.getDefault(), RequestType.GET_REST.getValue());

        String response = HTTPHelper.connectPost(HTTPHelper.REST_BACKEND, loadParams);
        availableRestaurants = JSONHelper.parseRestaurants(response);
    }

    private void loadOrder(User u){
    }

    public User getLoggedUser(){
        return user;
    }

    public boolean isLoggedIn(){
        return spManager.isLoggedIn();
    }

    public ArrayList<Restaurant> getRestaurants(){
        return this.availableRestaurants;
    }

    public boolean hasOpenOrder(){
        if (currentOrder != null) {
            return true;
        } else {
            return false;
        }
    }

    public Order getOrder(){
        if (hasOpenOrder()){
            return this.currentOrder;
        } else {
            return null;
        }
    }

    public void createOrder(Restaurant r){
        this.currentOrder = new Order(r);
    }

    public void closeOrder(){
        this.currentOrder = null;
    }

    public static DataModel fromJson(String in){
        return new Gson().fromJson(in, DataModel.class);
    }

    public String getJson(){
        return new Gson().toJson(this);
    }

}
