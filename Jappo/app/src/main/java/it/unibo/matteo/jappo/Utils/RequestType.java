package it.unibo.matteo.jappo.Utils;

public enum RequestType {

    LOGIN("LOGIN"), REGISTER("REGISTER"), GET_USER("GET_USER"),
    GET_FAV("GET_FAV"), GET_REST("GET_REST"), UPLOAD_FAV("UPLOAD_FAV"),
    GET_HIGHSCORES("GET_HIGHSCORES"), UPDATE_HIGHSCORE("UPDATE_HIGHSCORE");

    public final String value;

    RequestType(String value) {
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }

    public static String getDefault(){
        return "request_type";
    }
}