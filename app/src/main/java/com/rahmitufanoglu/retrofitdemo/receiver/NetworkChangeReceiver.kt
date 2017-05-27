package com.rahmitufanoglu.retrofitdemo.receiver


import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.widget.Toast

import com.rahmitufanoglu.retrofitdemo.SplashActivity
import com.rahmitufanoglu.retrofitdemo.util.NetworkUtil


class NetworkChangeReceiver : BroadcastReceiver() {

    private var mContext: Context? = null

    override fun onReceive(context: Context, intent: Intent) {
        mContext = context

        val isConnected = NetworkUtil.isNetworkConnected(context)
        val toast: Toast
        if (isConnected) {
            toast = Toast.makeText(context, "Internet connected", Toast.LENGTH_LONG)
            toast.show()
            mContext!!.startActivity(intent)
            (mContext as Activity).finish()
        } else {
            toast = Toast.makeText(mContext, "Internet connection lost", Toast.LENGTH_LONG)
            toast.show()
            checkInternet()
        }
    }

    private fun checkInternet() {
        // WifiManager wifi = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        // var wifi = mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
        val alertDialogBuilder = AlertDialog.Builder(mContext!!)
        alertDialogBuilder
                .setTitle("Internet Settings")
                .setMessage("No internet. \nCheck the internet connection and try again.")
                .setCancelable(false)
                .setNegativeButton("No") { dialog, id -> (mContext as Activity).finish() }
                //.setPositiveButton("YES", (dialog, id) -> wifi.setWifiEnabled(true))
                .setPositiveButton("YES") { dialog, id ->
                    (mContext as Activity).finish()
                    val splashIntent = Intent(mContext, SplashActivity::class.java)
                    mContext!!.startActivity(splashIntent)
                }
                .create()
                .show()
    }

}
