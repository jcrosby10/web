package com.huntergaming.web

import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * Provides options for internet connection status.
 */
class InternetStatus @Inject constructor() {

    /**
     * Callback for network status. Must register and unregister.
     */
    val networkCallBack: ConnectivityManager.NetworkCallback =
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                isConnected = true
                _isConnectedFlow.value = true
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                isConnected = false
                _isConnectedFlow.value = false
            }
        }

    /**
     * Is there a usable internet connection
     */
    var isConnected: Boolean = false

    private val _isConnectedFlow = MutableStateFlow(isConnected)
    /**
     * Be notified when connection state changes.
     */
    val isConnectedFlow: StateFlow<Boolean> = _isConnectedFlow.asStateFlow()
}