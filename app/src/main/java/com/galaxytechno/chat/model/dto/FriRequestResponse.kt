package com.galaxytechno.chat.model.dto

import com.galaxytechno.chat.model.vos.FriReqVos
import com.galaxytechno.chat.model.vos.Pageable

class FriRequestResponse(
    val status: String,
    val messageCode: Int,
    val message: String,
    val data: FriRequestData,
    val error: String
)

data class FriRequestData(
    val friendRequestList: List<FriReqVos>,
    val pageable: Pageable
)