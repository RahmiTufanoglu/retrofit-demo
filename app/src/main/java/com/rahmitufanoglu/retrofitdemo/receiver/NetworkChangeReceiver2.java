package com.rahmitufanoglu.retrofitdemo.receiver;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.rahmitufanoglu.retrofitdemo.SplashActivity2;
import com.rahmitufanoglu.retrofitdemo.util.NetworkUtil;


public class NetworkChangeReceiver2 extends BroadcastReceiver {

    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;

        boolean isConnected = NetworkUtil.isNetworkConnected(context);
        final Toast toast;
        if (isConnected) {
            toast = Toast.makeText(mContext, "Internet connected", Toast.LENGTH_LONG);
            toast.show();
            mContext.startActivity(intent);
            ((Activity) mContext).finish();
        } else {
            toast = Toast.makeText(mContext, "Internet connection lost", Toast.LENGTH_LONG);
            toast.show();
            checkInternet();
        }
    }

    private void checkInternet() {
        //WifiManager wifi = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder
                .setTitle("Internet Settings")
                .setMessage("No internet. \nCheck the internet connection and try again.")
                .setCancelable(false)
                .setNegativeButton("No", (dialog, id) -> ((Activity) mContext).finish())
                //.setPositiveButton("YES", (dialog, id) -> wifi.setWifiEnabled(true))
                .setPositiveButton("YES", (dialog, id) -> {
                    ((Activity) mContext).finish();
                    Intent splashIntent = new Intent(mContext, SplashActivity2.class);
                    mContext.startActivity(splashIntent);
                })
                .create()
                .show();
    }

}
