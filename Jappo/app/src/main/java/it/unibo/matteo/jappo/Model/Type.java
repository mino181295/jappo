package it.unibo.matteo.jappo.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import it.unibo.matteo.jappo.R;

public enum Type {
    @SerializedName("1") TEMPURA(1, "Tempura", R.drawable.type_1),
    @SerializedName("2") SASHIMI(2, "Sashimi", R.drawable.type_2),
    @SerializedName("3") TEMAKI(3, "Temaki", R.drawable.type_3),
    @SerializedName("4") HOSOMAKI(4, "Hosomaki", R.drawable.type_4),
    @SerializedName("5") URAMAKI(5, "Uramaki", R.drawable.type_5),
    @SerializedName("6") NIGHIRI(6, "Nighiri", R.drawable.type_6);

    private int number;
    private String name;
    private int imageSrc;

    Type(int number, String name, int imageSrc) {
        this.number = number;
        this.name = name;
        this.imageSrc = imageSrc;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return imageSrc;
    }

    public static Type fromNumber(int i){
        for (Type t : Type.values()) {
            if (i == t.getNumber()){
                return t;
            }
        }
        return null;
    }

    public static List<Type> getList(){
        List<Type> list = new ArrayList<Type>();
        for (Type t : Type.values()) {
            list.add(t);
        }
        return list;
    }
}
