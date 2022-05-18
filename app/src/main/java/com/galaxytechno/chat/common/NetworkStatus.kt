package com.galaxytechno.chat.common

sealed class NetworkStatus {
    object Available : NetworkStatus()
    object UnAvailable : NetworkStatus()
}
