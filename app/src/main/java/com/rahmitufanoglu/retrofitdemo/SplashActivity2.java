package com.rahmitufanoglu.retrofitdemo;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.rahmitufanoglu.retrofitdemo.receiver.NetworkChangeReceiver;


public class SplashActivity2 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent networkIntent = new Intent(getApplicationContext(), SliderActivity2.class);
        NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver();
        networkChangeReceiver.onReceive(this, networkIntent);
    }

}
