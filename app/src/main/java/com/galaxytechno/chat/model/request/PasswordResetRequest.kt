package com.galaxytechno.chat.model.request

data class PasswordResetRequest(
    val countryId: Int,
    val mobile: String,
    val newPassword: String,
    val confirmPassword: String,
    val locale: Int
)
