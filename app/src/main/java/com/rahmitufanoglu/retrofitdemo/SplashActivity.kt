package com.rahmitufanoglu.retrofitdemo


import android.app.Activity
import android.content.Intent
import android.os.Bundle

import com.rahmitufanoglu.retrofitdemo.receiver.NetworkChangeReceiver


class SplashActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val networkIntent = Intent(applicationContext, SliderActivity::class.java)
        val networkChangeReceiver = NetworkChangeReceiver()
        networkChangeReceiver.onReceive(this, networkIntent)
    }

}
