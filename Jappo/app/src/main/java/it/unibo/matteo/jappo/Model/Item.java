package it.unibo.matteo.jappo.Model;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Item {

    private String name;
    private int number;
    private Type type;
    private Restaurant restaurant;

    private boolean isFavorite;

    private Date time;

    public Item(String name, int number, Type type, Restaurant restaurant) {
        this.name = name;
        this.number = number;
        this.type = type;
        this.restaurant = restaurant;
    }

    public String getJson(){
        return new Gson().toJson(this);
    }

    public static Item fromJson(String in){
        return new Gson().fromJson(in, Item.class);
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public Type getType() {
        return type;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Date getTime() {
        return this.time;
    }

    public String getTimeString() {
        return dateToString(this.time);
    }

    public static Date getCurrentTime(){
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        Date currentLocalTime = cal.getTime();
        return currentLocalTime;
    }

    public static String dateToString(Date d){
        DateFormat date = new SimpleDateFormat("HH:mm");
        date.setTimeZone(TimeZone.getDefault());

        return date.format(d);
    }

    public boolean isFavorite(List<Item> fav) {
        return fav.contains(this);
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (number != item.number) return false;
        if (name != null ? !name.equals(item.name) : item.name != null) return false;
        return type == item.type;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + number;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
