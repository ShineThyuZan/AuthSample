package com.galaxytechno.chat.model.dto

import com.galaxytechno.chat.model.response.BaseResponse

data class ValidateOtpResponse(
    override val status: String,
    override val messageCode: Int,
) : BaseResponse<OTPValidateData>()

data class OTPValidateData(
    val otpCheckSuccess: Boolean
)


