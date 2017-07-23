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

    /**
     * Function that parse a response and check if is correct or not
     * @param response
     * @param requestType
     * @return
     */
    public static boolean parseResponse(String response, RequestType requestType) {
        JSONObject object;
        boolean isCorrect = false;

        try {
            switch (requestType) {
                case LOGIN:
                    object = new JSONObject(response);
                    isCorrect = object.getBoolean("success");
                    return isCorrect;
                case REGISTER:
                    object = new JSONObject(response);
                    isCorrect = object.getBoolean("success");
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

    /**
     * Method that parse a {@link User}
     * @param response {@link String} of the response from the server
     * @return the object of the result user
     */
    public static User parseUser(String response){
        try {
            JSONObject object = new JSONObject(response);
            int id = object.getInt("ID");
            String name = object.getString("NAME");
            String email = object.getString("MAIL");
            return new User(id, name, email);
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * Parse all the favourites of the application registered {@link User} on the server
     * @param response
     * @return
     */
    public static ArrayList<Item> parseFavorites(String response){
        ArrayList<Item> favorites = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(response);
            for(int i = 0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                String name = object.getString("NAME");
                int number = object.getInt("NUMBER");
                int type = object.getInt("TYPE");
                favorites.add(new Item(name,number, Type.fromNumber(type),null));
            }
        } catch (JSONException e) {
            return null;
        } finally {
            return favorites;
        }
    }

    /**
     * Parse all the highscores and return an array of {@link Score}
     * @param response
     * @return
     */
    public static ArrayList<Score> parseHighscores(String response){
        ArrayList<Score> highscores = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(response);
            for(int i = 0; i < array.length(); i++){
                JSONObject jsonObject = array.getJSONObject(i);
                String name = jsonObject.getString("NAME");
                String date = jsonObject.getString("DATE");
                int value = jsonObject.getInt("SCORE");
                highscores.add(new Score(name, value, date));
            }
        } catch (JSONException e) {
            return null;
        } finally {
            return highscores;
        }
    }

    /**
     * Parse all the {@link Restaurant} in the backend database
     * @param response
     * @return
     */
    public static ArrayList<Restaurant> parseRestaurants(String response){
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(response);
            for(int i = 0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                String address = object.getString("ADDRESS");
                String name = object.getString("NAME");
                String city = object.getString("CITY");
                float lat = Float.parseFloat(object.getString("LATITUDE"));
                float log = Float.parseFloat(object.getString("LONGITUDE"));
                restaurants.add(new Restaurant(name, address, city, lat, log));
            }
        } catch (JSONException e) {
            return null;
        } finally {
            return restaurants;
        }
    }
}
