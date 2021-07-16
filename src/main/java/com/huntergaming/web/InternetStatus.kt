package com.huntergaming.web

import android.net.ConnectivityManager
import android.net.Network
import javax.inject.Inject

class InternetStatus @Inject constructor() {

    val networkCallBack: ConnectivityManager.NetworkCallback =
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                isConnected = true
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                isConnected = false
            }
        }

    var isConnected: Boolean = false
}