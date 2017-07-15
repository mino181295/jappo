package it.unibo.matteo.jappo.Utils;

import org.json.JSONException;
import org.json.JSONObject;

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
            String name = jObject.getString("NAME");
            String email = jObject.getString("MAIL");
            String imageName = jObject.getString("IMAGE");
            return new User(name, email, imageName);
        } catch (JSONException e) {
            return null;
        }
    }
}
