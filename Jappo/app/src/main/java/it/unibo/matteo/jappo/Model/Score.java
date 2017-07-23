package it.unibo.matteo.jappo.Model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Model item for a score value with a {@link Date}, a name and a score value.
 */
public class Score {

    private String name;
    private Date date;
    private int value;

    private DateFormat dateFormat;

    public Score(String name, int value, String date){
        this.name = name;
        this.value = value;
        this.dateFormat = new SimpleDateFormat("HH:mm_dd-MM-yyyy");
        dateFormat.setTimeZone(TimeZone.getDefault());
        try{
            this.date = dateFormat.parse(date);
        } catch (ParseException e){}
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public String getDate() {
        Date nowDate = new Date();
        long secondsDifference = (nowDate.getTime() - date.getTime()) / 1000;
        /* Data representation */
        if (secondsDifference < 60 * 60 * 24) {
            return new SimpleDateFormat("HH:mm").format(date);
        } else {
            return new SimpleDateFormat("dd-MM-yy").format(date);
        }
    }
}
