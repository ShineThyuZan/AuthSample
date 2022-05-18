package com.galaxytechno.chat.model.dto

import com.galaxytechno.chat.model.vos.FriListVos
import com.galaxytechno.chat.model.vos.Pageable

class BlockListResponse(
    val status: String,
    val messageCode: String,
    val message: String,
    val data: Data
)

class Data(
    val friendList: List<FriListVos>,
    val pageable: Pageable
)