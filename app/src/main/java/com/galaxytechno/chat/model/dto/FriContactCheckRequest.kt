package com.galaxytechno.chat.model.dto

class FriContactCheckRequest(
    val userId: Long,
    val locale: Int,
    val mobiles: List<String>
)