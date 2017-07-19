package it.unibo.matteo.jappo.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.unibo.matteo.jappo.Model.Item;
import it.unibo.matteo.jappo.Model.Restaurant;
import it.unibo.matteo.jappo.Model.Score;
import it.unibo.matteo.jappo.Model.Type;
import it.unibo.matteo.jappo.Model.User;

import static android.R.attr.type;
import static it.unibo.matteo.jappo.R.string.number;

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
            return new User(id, name, email);
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

    public static ArrayList<Score> parseHighscores(String response){
        ArrayList<Score> highscores = new ArrayList<>();
        try {
            JSONArray jArray = new JSONArray(response);
            for(int i = 0; i < jArray.length(); i++){
                JSONObject jsonObject = jArray.getJSONObject(i);
                String name = jsonObject.getString("NAME");
                int value = jsonObject.getInt("SCORE");
                String date = jsonObject.getString("DATE");
                highscores.add(new Score(name, value, date));
            }
        } catch (JSONException e) {
            return null;
        } finally {
            return highscores;
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
