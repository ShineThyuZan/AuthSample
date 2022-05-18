package com.galaxytechno.chat.model.dto

import com.galaxytechno.chat.model.response.BaseResponse

data class RefreshTokenDTO(
    override val status: String,
    override val messageCode: Int,
) : BaseResponse<RefreshTokenData>()

data class RefreshTokenData(
    val refreshToken: String,
    val accessToken: String
)