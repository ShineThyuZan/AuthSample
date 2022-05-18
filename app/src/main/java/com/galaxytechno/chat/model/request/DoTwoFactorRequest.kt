package com.galaxytechno.chat.model.request

class DoTwoFactorRequest(
    val userId: Long,
    val isTwoFactor: Boolean,
    val locale: Int
)