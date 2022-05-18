package com.galaxytechno.chat.model.dto

data class FriendAddResponse(
        val status: String,
        val messageCode: Int,
        val message: String,
        val error : String
)