package com.galaxytechno.chat.model.dto

data class MessageObj(
    val headUrl: String,
    val messageContent: String,
    val messageType: Int,
    val contentType: Int,
    val receiverId: Long,
    val viewType: Int
)
