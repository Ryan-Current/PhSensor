package com.rcdevelopment.phsensor;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesAccess {

    private Context context;

    public static final String SHARED_PREFERENCES_NAME = "com.rcdevelopment.phsensor.sharedprefs";

    public static final String B_PREFERENCES_KEY = "com.rcdevelopment.phsensor.b";
    public static final String M_PREFERENCES_KEY = "com.rcdevelopment.phsensor.m";

    private SharedPreferences sharedPreferences;


    public SharedPreferencesAccess(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public double LoadB() {
        return (double)sharedPreferences.getFloat(B_PREFERENCES_KEY, 0.0f);
    }


    public double LoadM() {
        return (double)sharedPreferences.getFloat(M_PREFERENCES_KEY, 0.0f);
    }


    public void SaveB(double b) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(B_PREFERENCES_KEY, (float)b);
        editor.apply();
    }


    public void SaveM(double m) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(M_PREFERENCES_KEY, (float)m);
        editor.apply();
    }

}

