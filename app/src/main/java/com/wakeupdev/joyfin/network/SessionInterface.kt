package com.wakeupdev.joyfin.network


interface SessionInterface {
    fun onAuthTokenExpired()
    fun onSessionTimeout()
}