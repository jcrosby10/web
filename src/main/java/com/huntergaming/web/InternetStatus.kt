package com.huntergaming.web

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class InternetStatus @Inject constructor(
    @ApplicationContext private val context: Context
) {

    // properties

    private val _internetConnectionStatus = MutableStateFlow(InternetConnectionStatus.UNKNOWN)
    val internetConnectionStatus: StateFlow<InternetConnectionStatus> = _internetConnectionStatus

    private val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            _internetConnectionStatus.value = InternetConnectionStatus.CONNECTED
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            _internetConnectionStatus.value = InternetConnectionStatus.DISCONNECTED
        }
    }

    // functions

    fun register() {
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
        connectivityManager.requestNetwork(networkRequest, networkCallback)
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    fun unregister() {
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}

// enums

enum class InternetConnectionStatus {
    CONNECTED, DISCONNECTED, UNKNOWN
}