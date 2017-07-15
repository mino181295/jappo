package it.unibo.matteo.jappo.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import it.unibo.matteo.jappo.Model.User;

public class SharedPreferencesManager {

    public static String sharedPreferencesName;

    private SharedPreferences mSP;
    private SharedPreferences.Editor mEditor;

    public static final String IS_LOGGED = "LOGGED";
    public static final String USER = "USER";

    public SharedPreferencesManager(String name, Context context) {
        this.sharedPreferencesName = name;
        this.mSP = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        this.mEditor = mSP.edit();
    }

    public boolean isLoggedIn(){
        if (mSP.contains(IS_LOGGED)){
            return mSP.getBoolean(IS_LOGGED, false);
        } else {
            return false;
        }
    }

    public User getLoggedUser() {
        if (isLoggedIn()){
            String stringUser = null;
            stringUser = mSP.getString(USER, stringUser);
            return User.fromJson(stringUser);
        } else return null;
    }

    public void setLoggedUser(User u) {
        String stringUser = u.getJson();
        mEditor.putString(USER, stringUser);
        mEditor.putBoolean(IS_LOGGED, true);
        mEditor.apply();
    }

    public void logoutLoggedUser(){
        mEditor.remove(USER);
        mEditor.putBoolean(IS_LOGGED, false);
        mEditor.apply();
    }
}
