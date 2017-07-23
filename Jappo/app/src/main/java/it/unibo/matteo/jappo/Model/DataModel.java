package it.unibo.matteo.jappo.Model;

import android.content.Context;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import it.unibo.matteo.jappo.Utils.HTTPHelper;
import it.unibo.matteo.jappo.Utils.JSONHelper;
import it.unibo.matteo.jappo.Utils.RequestType;
import it.unibo.matteo.jappo.Utils.SharedPreferencesManager;

import static it.unibo.matteo.jappo.Utils.HTTPHelper.connectPost;

public class DataModel {

    private static final String SP_NAME = "Default";

    private static SharedPreferencesManager spManager;

    private User user;
    private Order order;
    private ArrayList<Restaurant> restaurants;

    public DataModel(Context context){
        spManager = new SharedPreferencesManager(SP_NAME, context);

        user = null;
        order = null;
        restaurants = null;
    }

    public void load(){
        if (spManager.isLoggedIn()){
            user = spManager.getLoggedUser();
            if (spManager.hasDataModel()) {
                DataModel dm = spManager.loadDataModel();
                user = dm.user;
                order = dm.order;
                restaurants = dm.restaurants;
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

        String response = connectPost(HTTPHelper.REST_BACKEND, loadParams);
        ArrayList<Item> favorites = JSONHelper.parseFavorites(response);
        u.setFavorites(favorites);
    }

    private void loadRestaurants(){
        HashMap<String, String> loadParams = new HashMap<>();
        loadParams.put(RequestType.getDefault(), RequestType.GET_REST.getValue());

        String response = connectPost(HTTPHelper.REST_BACKEND, loadParams);
        restaurants = JSONHelper.parseRestaurants(response);
    }

    public void uploadFavorites(){
        HashMap<String, String> loadParams = new HashMap<>();
        loadParams.put(RequestType.getDefault(), RequestType.UPLOAD_FAV.getValue());
        loadParams.put("fav", new Gson().toJson(user.getFavorites()));
        loadParams.put("user", String.valueOf(user.getId()));

        connectPost(HTTPHelper.REST_BACKEND, loadParams);
    }

    public boolean isLoggedIn(){
        return spManager.isLoggedIn();
    }

    public User getLoggedUser(){
        return user;
    }

    public boolean hasOpenOrder(){
        if (order != null) {
            return true;
        } else {
            return false;
        }
    }

    public Order getOrder(){
        if (hasOpenOrder()){
            return this.order;
        } else {
            return null;
        }
    }

    public void createOrder(Restaurant r){
        this.order = new Order(r);
    }

    public void closeOrder(){
        this.order = null;
    }

    public ArrayList<Restaurant> getRestaurants(){
        return this.restaurants;
    }

    public ArrayList<Score> getHighscores(){
        HashMap<String, String> params = new HashMap<>();
        params.put(RequestType.getDefault(), RequestType.GET_HIGHSCORES.getValue());

        String response = connectPost(HTTPHelper.REST_BACKEND, params);
        ArrayList<Score> highscores = JSONHelper.parseHighscores(response);
        return highscores;
    }

    public void updateUserHighScore(int value){
        HashMap<String, String> params = new HashMap<>();
        params.put(RequestType.getDefault(), RequestType.UPDATE_HIGHSCORE.getValue());
        params.put("number", String.valueOf(value));
        params.put("id", String.valueOf(user.getId()));

        connectPost(HTTPHelper.REST_BACKEND, params);
    }

    public static DataModel fromJson(String in){
        return new Gson().fromJson(in, DataModel.class);
    }

    public String getJson(){
        return new Gson().toJson(this);
    }
}
