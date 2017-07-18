package it.unibo.matteo.jappo.Model;

import android.content.Context;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.unibo.matteo.jappo.Utils.HTTPHelper;
import it.unibo.matteo.jappo.Utils.JSONHelper;
import it.unibo.matteo.jappo.Utils.RequestType;
import it.unibo.matteo.jappo.Utils.SharedPreferencesManager;

public class DataModel {

    private static final String SP_NAME = "Default";
    private static SharedPreferencesManager spManager;

    private User user;
    private Order currentOrder;
    private ArrayList<Restaurant> availableRestaurants;

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
        spManager.logoutLoggedUser();
        spManager.clearDataModel();
    }

    public void login(User u){
        user = u;
        spManager.setLoggedUser(user);
    }

    public void loadData(User user){
        loadRestaurants();
        loadFavorites(user);
    }

    private void loadFavorites(User u){
        HashMap<String, String> loadParams = new HashMap<>();
        loadParams.put(RequestType.getDefault(), RequestType.GET_FAV.getValue());
        loadParams.put("id", String.valueOf(u.getId()));

        String response = HTTPHelper.connectPost(HTTPHelper.REST_BACKEND, loadParams);
        ArrayList<Item> favorites = JSONHelper.parseFavorites(response);
        u.setFavorites(favorites);
    }

    private void loadRestaurants(){
        HashMap<String, String> loadParams = new HashMap<>();
        loadParams.put(RequestType.getDefault(), RequestType.GET_REST.getValue());

        String response = HTTPHelper.connectPost(HTTPHelper.REST_BACKEND, loadParams);
        availableRestaurants = JSONHelper.parseRestaurants(response);
    }

    public void uploadFavorites(){
        HashMap<String, String> loadParams = new HashMap<>();
        loadParams.put(RequestType.getDefault(), RequestType.UPLOAD_FAV.getValue());
        loadParams.put("fav", new Gson().toJson(user.getFavorites()));
        loadParams.put("user", String.valueOf(user.getId()));

        String response = HTTPHelper.connectPost(HTTPHelper.REST_BACKEND, loadParams);
    }

    public boolean isLoggedIn(){
        return spManager.isLoggedIn();
    }

    public User getLoggedUser(){
        return user;
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

    public ArrayList<Restaurant> getRestaurants(){
        return this.availableRestaurants;
    }

    public ArrayList<Score> getHighscores(){
        HashMap<String, String> params = new HashMap<>();
        params.put(RequestType.getDefault(), RequestType.GET_HIGHSCORES.getValue());

        String response = HTTPHelper.connectPost(HTTPHelper.REST_BACKEND, params);
        ArrayList<Score> highscores = JSONHelper.parseHighscores(response);
        return highscores;
    }

    public static DataModel fromJson(String in){
        return new Gson().fromJson(in, DataModel.class);
    }

    public String getJson(){
        return new Gson().toJson(this);
    }

}
