package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat

data class JoinRoom(
    val userId: String,
    val conversationId: String
)

data class ChatMessage(
    val msgContent: String
)

data class LeaveRoom(
    val userId: String,
    val conversationId: String
)

