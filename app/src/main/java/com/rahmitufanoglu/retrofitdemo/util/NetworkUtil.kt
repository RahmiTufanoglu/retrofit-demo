package com.rahmitufanoglu.retrofitdemo.util


import android.content.Context
import android.net.ConnectivityManager


class NetworkUtil {

    companion object {
        @JvmStatic fun isNetworkConnected(context: Context): Boolean { // @JvmStatic damit es in Java funktioniert
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    }

}
