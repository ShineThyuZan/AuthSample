package com.galaxytechno.chat.model.dto

import com.galaxytechno.chat.model.response.BaseResponse

data class CheckAccountByMobileResponse(
    override val status: String,
    override val messageCode: Int,
) : BaseResponse<AccountStatus>()

data class AccountStatus(
    val name: String,
    val headUrl: String,
    val twoFactor: Boolean,
)