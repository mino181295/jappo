package it.unibo.matteo.jappo.Model;

import android.content.Context;

import java.util.ArrayList;

import it.unibo.matteo.jappo.Utils.SharedPreferencesManager;


public class DataModel {

    public static final String SP_NAME = "Default";

    User user;
    Order currentOrder;
    ArrayList<Restaurant> availableRestaurants;

    SharedPreferencesManager spManager;
    private static DataModel INSTANCE = null;

    private DataModel(Context context){
        spManager = new SharedPreferencesManager(SP_NAME, context);
        if (spManager.isLoggedIn()){
            this.user = spManager.getLoggedUser();
        }
        loadData(this.user);
    }

    public static DataModel getInstance(Context context){
        if (INSTANCE != null){
            return INSTANCE;
        } else {
            INSTANCE = new DataModel(context);
            return INSTANCE;
        }
    }

    private void loadData(User user){
        loadRestourants();
        loadFavorites(user);
        loadOrder(user);
    }

    private static void loadFavorites(User u){

    }

    private static void loadOrder(User u){

    }

    private static void loadRestourants(){

    }

}
