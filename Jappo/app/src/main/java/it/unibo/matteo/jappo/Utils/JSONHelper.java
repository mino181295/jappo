package it.unibo.matteo.jappo.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.unibo.matteo.jappo.Model.Item;
import it.unibo.matteo.jappo.Model.Restaurant;
import it.unibo.matteo.jappo.Model.Type;
import it.unibo.matteo.jappo.Model.User;

import static android.R.attr.type;

public class JSONHelper {

    public static boolean parseResponse(String response, RequestType requestType) {
        JSONObject jObject;
        boolean isCorrect = false;

        try {
            switch (requestType) {
                case LOGIN:
                    jObject = new JSONObject(response);
                    isCorrect = jObject.getBoolean("success");
                    return isCorrect;
                case REGISTER:
                    jObject = new JSONObject(response);
                    isCorrect = jObject.getBoolean("success");
                    return isCorrect;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return isCorrect;
    }

    public static User parseUser(String response){
        try {
            JSONObject jObject = new JSONObject(response);
            int id = jObject.getInt("ID");
            String name = jObject.getString("NAME");
            String email = jObject.getString("MAIL");
            String imageName = jObject.getString("IMAGE");
            return new User(id, name, email, imageName);
        } catch (JSONException e) {
            return null;
        }
    }

    public static ArrayList<Item> parseFavorites(String response){
        ArrayList<Item> fav = new ArrayList<>();
        try {
            JSONArray jArray = new JSONArray(response);
            for(int i = 0; i < jArray.length(); i++){
                JSONObject jsonObject = jArray.getJSONObject(i);
                String name = jsonObject.getString("NAME");
                int number = jsonObject.getInt("NUMBER");
                int type = jsonObject.getInt("TYPE");
                fav.add(new Item(name,number, Type.fromNumber(type),null));
            }
        } catch (JSONException e) {
            return null;
        } finally {
            return fav;
        }
    }

    public static ArrayList<Restaurant> parseRestaurants(String response){
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        try {
            JSONArray jArray = new JSONArray(response);
            for(int i = 0; i < jArray.length(); i++){
                JSONObject jsonObject = jArray.getJSONObject(i);
                String name = jsonObject.getString("NAME");
                String address = jsonObject.getString("ADDRESS");
                String city = jsonObject.getString("CITY");
                float lat = Float.parseFloat(jsonObject.getString("LATITUDE"));
                float log = Float.parseFloat(jsonObject.getString("LONGITUDE"));
                restaurants.add(new Restaurant(name, address, city, lat, log));
            }
        } catch (JSONException e) {
            return null;
        } finally {
            return restaurants;
        }
    }
}
