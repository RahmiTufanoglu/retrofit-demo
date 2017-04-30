package com.rahmitufanoglu.retrofitdemo;


import android.content.Context;
import android.content.SharedPreferences;


public class PreferenceManager {

    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private static final int PRIVATE_MODE = 0;
    private static final String PREFERENCE_NAME = "intro_slider";
    private static final String IS_FIRST_TIME_LAUNCH = "is:_first_time_launch";

    public PreferenceManager(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME, PRIVATE_MODE);
        //mSharedPreferencesEditor = mSharedPreferences.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTimeLaunch) {
        SharedPreferences.Editor sharedPreferencesEditor = mSharedPreferences.edit();
        sharedPreferencesEditor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTimeLaunch);
        sharedPreferencesEditor.apply();
    }

    public boolean isFirstTimeLaunch() {
        return mSharedPreferences.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
}
