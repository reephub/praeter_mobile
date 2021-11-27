package com.reephub.praeter.core.interfaces

interface ConnectivityListener {
    fun onConnected()

    fun onLostConnection()
}