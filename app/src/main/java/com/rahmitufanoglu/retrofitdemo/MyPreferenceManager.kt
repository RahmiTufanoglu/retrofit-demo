package com.rahmitufanoglu.retrofitdemo


import android.content.Context
import android.content.SharedPreferences


class MyPreferenceManager(mContext: Context) {

    private val mSharedPreferences: SharedPreferences

    companion object {
        private val PRIVATE_MODE = 0
        private val PREFERENCE_NAME = "intro_slider"
        private val IS_FIRST_TIME_LAUNCH = "is:_first_time_launch"
    }

    init {
        mSharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME, PRIVATE_MODE)
        //mSharedPreferencesEditor = mSharedPreferences.edit();
    }

    fun setFirstTimeLaunch(isFirstTimeLaunch: Boolean) {
        val sharedPreferencesEditor = mSharedPreferences.edit()
        sharedPreferencesEditor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTimeLaunch)
        sharedPreferencesEditor.apply()
    }

    fun isFirstTimeLaunch(): Boolean {
        return mSharedPreferences.getBoolean(IS_FIRST_TIME_LAUNCH, true)
    }
}
