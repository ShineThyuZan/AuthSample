package com.galaxytechno.chat.model.response

data class VerifiedResponse(
    val status : String,
    val messageCode : Int,
    val message : String? = null,
    val error : String? = null
)

