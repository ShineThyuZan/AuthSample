package com.galaxytechno.chat.model.dto

    data class UnfriendResponse(
        val status: String,
        val messageCode: Int,
        val message: String,
        val error : String
    )