package com.galaxytechno.chat.model.dto

import com.galaxytechno.chat.model.response.BaseResponse

data class GetOtpResponse(
    override val status: String,
    override val messageCode: Int,
) : BaseResponse<OtpObj>()

data class OtpObj(
    val otpCode: String,
    val expireTimeMin: Int,
    val isRegister: Boolean
)