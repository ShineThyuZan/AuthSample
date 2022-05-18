package com.galaxytechno.chat.presentation.extension

/**

STATE - visibility, activated state,etc.
EVENT - onClick, onEventChanged,etc.

 */

sealed class RemoteEvent<T>(
    val data: T? = null,
    val message: String? = null
) {
    class LoadingEvent<T> : RemoteEvent<T>()
    class ErrorEvent<T>(errorMessage: String) : RemoteEvent<T>(null, errorMessage)
    class SuccessEvent<T>(data: T) : RemoteEvent<T>(data)
}

