package com.galaxytechno.chat.model.dto


class FriendRequestConfirmRequest(
    val userId: Long,
    val friendId: Long,
    val isAccept: Boolean,
    val locale: Int
)