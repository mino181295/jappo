package it.unibo.matteo.jappo.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import it.unibo.matteo.jappo.Model.DataModel;
import it.unibo.matteo.jappo.Model.User;

public class SharedPreferencesManager {

    public static String sharedPreferencesName;

    private SharedPreferences mSP;
    private SharedPreferences.Editor mEditor;

    public static final String DATA_MODEL = "DATA_MODEL";
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

    public boolean hasDataModel(){
        return mSP.contains(DATA_MODEL);
    }

    public DataModel loadDataModel(){
        if (mSP.contains(DATA_MODEL)){
            String dataModel = null;
            dataModel = mSP.getString(DATA_MODEL, dataModel);
            return DataModel.fromJson(dataModel);
        } else return null;
    }

    public void writeDataModel(DataModel dm){
        String stringDataModel = dm.getJson();
        mEditor.putString(DATA_MODEL, stringDataModel);
        mEditor.apply();
    }

    public void clearDataModel(){
        mEditor.remove(DATA_MODEL);
        mEditor.apply();
    }
}
