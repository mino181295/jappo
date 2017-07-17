package it.unibo.matteo.jappo.Utils;

import android.content.Context;
import android.os.Vibrator;

public class VibratorManager {

    public static int LONG = 180;
    public static int MEDIUM = 120;
    public static int SHORT = 70;

    public static void makeBuzz(Context c, int milliseconds){
        Vibrator v = (Vibrator) c.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(milliseconds);
    }
}
