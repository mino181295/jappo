package it.unibo.matteo.jappo.Utils;

public enum RequestType {


    LOGIN("LOGIN"), REGISTER("REGISTER");

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