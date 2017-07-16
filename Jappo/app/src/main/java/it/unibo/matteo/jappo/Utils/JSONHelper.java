package it.unibo.matteo.jappo.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.unibo.matteo.jappo.Model.Item;
import it.unibo.matteo.jappo.Model.Type;
import it.unibo.matteo.jappo.Model.User;

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
                String typename = jsonObject.getString("TYPENAME");
                fav.add(new Item(name,number,
                        new Type(typename, false, false, typename+".jpg"),null));
            }
        } catch (JSONException e) {
            return null;
        } finally {
            return fav;
        }
    }
}
