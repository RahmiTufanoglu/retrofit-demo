package com.rahmitufanoglu.retrofitdemo;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent networkIntent = new Intent(getApplicationContext(), SliderActivity.class);
        NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver();
        networkChangeReceiver.onReceive(this, networkIntent);
    }
}
