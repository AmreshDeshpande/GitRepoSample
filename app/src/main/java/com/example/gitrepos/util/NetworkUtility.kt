package com.example.gitrepos.util

import android.app.Application
import android.arch.lifecycle.LiveData
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.net.NetworkRequest
import android.os.Build

object NetworkUtility : LiveData<Boolean>() {

    private lateinit var application: Application

    private lateinit var connectivityManager: ConnectivityManager

    private lateinit var networkCallback: ConnectivityManager.NetworkCallback


    fun init(application: Application) {
        this.application = application
    }

    fun registerNetworkCallback() {
        connectivityManager = this.application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network?) {
                postValue(true)
            }

            override fun onLost(network: Network?) {
                postValue(false)
            }
        }
    }

    override fun onActive() {
        super.onActive()

        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        postValue(activeNetwork?.isConnected == true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            val builder = NetworkRequest.Builder()
            connectivityManager.registerNetworkCallback(builder.build(), networkCallback)
        }
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

}