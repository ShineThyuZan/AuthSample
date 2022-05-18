package com.galaxytechno.chat.model.response

abstract class BaseResponse<T> {
    abstract val status: String
    abstract val messageCode: Int
    val data: T? = null
    val message: String? = null
    val error: String? = null
}