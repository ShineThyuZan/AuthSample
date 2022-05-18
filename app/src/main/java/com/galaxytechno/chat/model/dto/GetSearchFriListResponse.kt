package com.galaxytechno.chat.model.dto

import com.galaxytechno.chat.model.vos.FriListVos
import com.galaxytechno.chat.model.vos.Pageable

class GetSearchFriListResponse(
    val status: String,
    val messageCode: Int,
    val message: String,
    val data: SearchFriData
)

data class SearchFriData(
    val profileInfoList: MutableList<UserVos>? = null,
    val pageable: Pageable
)
