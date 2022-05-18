package com.galaxytechno.chat.model.dto

import com.galaxytechno.chat.model.vos.FriListVos
import com.galaxytechno.chat.model.vos.Pageable

class GetFriendListResponse(
    val status: String,
    val messageCode: Int,
    val message: String,
    val data: FriData
)
data class FriData(
    val friendList: MutableList<FriListVos>? = null,
    val pageable: Pageable
)
