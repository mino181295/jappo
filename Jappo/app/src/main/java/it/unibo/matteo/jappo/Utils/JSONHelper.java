package it.unibo.matteo.jappo.Utils;

import org.json.JSONException;
import org.json.JSONObject;

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
}
