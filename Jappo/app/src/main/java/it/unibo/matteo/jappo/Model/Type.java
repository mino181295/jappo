package it.unibo.matteo.jappo.Model;

import it.unibo.matteo.jappo.R;

public enum Type {

    TEMPURA(1, "Tempura", R.drawable.type_1),
    SASHIMI(2, "Sashimi", R.drawable.type_2),
    TEMAKI(3, "Temaki", R.drawable.type_3),
    HOSOMAKI(4, "Hosomaki", R.drawable.type_4),
    URAMAKI(5, "Uramaki", R.drawable.type_5),
    NIGHIRI(6, "Nighiri", R.drawable.type_6);

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

}
