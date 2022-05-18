package com.galaxytechno.chat.model.vos

class FriReqVos(
    val friendRequestId: Int,
    val requesterId: Long,
    val name: String,
    val headUrl: String,
    val requestedTime: String
)