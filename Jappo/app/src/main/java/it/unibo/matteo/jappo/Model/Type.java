package it.unibo.matteo.jappo.Model;

import com.google.gson.Gson;

public class Type {

    private String name;

    private boolean isRaw;
    private boolean isFried;

    private String imageName;

    public Type(String name, boolean isRaw, boolean isFried, String imageName) {
        this.name = name;
        this.isRaw = isRaw;
        this.isFried = isFried;
        this.imageName = imageName;
    }

    public String getJson(){
        return new Gson().toJson(this);
    }

    public static Type fromJson(String in){
        return new Gson().fromJson(in, Type.class);
    }

}
