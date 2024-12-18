@file:Suppress("DEPRECATION")

package com.example.newsapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo


public class NetworkReceiver(private val callback: (Boolean) -> Unit):BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            // Get the current network status
            val connectivityManager =
                context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            val isOnline = activeNetwork?.isConnected == true
            callback(isOnline)

        }
    }
}