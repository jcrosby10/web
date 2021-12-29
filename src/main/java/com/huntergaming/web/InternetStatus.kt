package com.huntergaming.web

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext

fun isConnected(@ApplicationContext context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

    if (connectivityManager != null) {
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {
            when {
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }
                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true
                }
                hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return true
                }
            }
        }
    }

    return false
}