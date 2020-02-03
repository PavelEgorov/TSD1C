package com.egorovsoft.tsd1c.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.egorovsoft.tsd1c.MainPresenter;

public class SPreference {
    private final static String TAG = "SPreference";

    private final static Object sync = new Object();
    private static SPreference instance;

    public static final String FILEDIR = "filedir";
    public final static String LANGUAGE = "language";
    public final static String SETTINGS = "settings";

    private SharedPreferences sharedPreferences;

    private SPreference(){
        Log.d(TAG, "SPreference: ");
    }

    public static SPreference getInstance(){
        Log.d(TAG, "getInstance: ");
        synchronized (sync){
            if (instance == null) instance = new SPreference();

            return instance;
        }
    }

    public void setSharedPreference(Context context, String name){
        Log.d(TAG, "setSharedPreference: name: " + name);
        synchronized (sync) {
            sharedPreferences = context.getSharedPreferences(name, context.MODE_PRIVATE);
        }
    }
    public void saveInt(String key, int value){
        Log.d(TAG, "savePreference: key: " + key + "; value: " + value);
        if (sharedPreferences == null) return;

        sharedPreferences.edit().putInt(key, value).commit();
    }

    public int loadInt(String key){
        Log.d(TAG, "loadInt: key: " + key);
        int value = -1;

        if (key == SPreference.LANGUAGE) value = sharedPreferences.getInt(key, MainPresenter.LANGUAGE_US);

        Log.d(TAG, "loadInt: value: " + value);
        return value;
    }

    public void saveString(String key, String value){
        Log.d(TAG, "savePreference: key: " + key + "; value: " + value);
        if (sharedPreferences == null) return;

        sharedPreferences.edit().putString(key, value).commit();
    }

    public String loadString(String key){
        Log.d(TAG, "loadInt: key: " + key);
        String value = "";

        if (key == SPreference.FILEDIR) value = sharedPreferences.getString(key, "");

        Log.d(TAG, "loadInt: value: " + value);
        return value;
    }
}
