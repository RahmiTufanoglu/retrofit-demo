package com.rahmitufanoglu.retrofitdemo;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;


public class NetworkChangeReceiver extends BroadcastReceiver {

    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;

        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnected();
        if (isConnected) {
            Toast.makeText(mContext, "Internet connected", Toast.LENGTH_LONG).show();
            mContext.startActivity(intent);
            ((Activity) mContext).finish();
        } else {
            Toast.makeText(mContext, "Internet connection lost", Toast.LENGTH_LONG).show();
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
                    Intent splashIntent = new Intent(mContext, SplashActivity.class);
                    mContext.startActivity(splashIntent);
                })
                .create()
                .show();
    }
}
