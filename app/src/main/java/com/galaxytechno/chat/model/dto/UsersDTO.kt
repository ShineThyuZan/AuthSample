package com.galaxytechno.chat.model.dto

import com.galaxytechno.chat.model.response.BaseResponse
import com.galaxytechno.chat.model.vos.AppUserVo
import com.galaxytechno.chat.model.vos.FriListVos
import com.galaxytechno.chat.model.vos.Pageable

data class UsersDTO(
    override val status: String,
    override val messageCode: Int
) : BaseResponse<UsersData>()

data class UsersData(
    val profileInfoList: List<AppUser>,
    val pageable: Pageable
)

data class AppUser(
    val userId: Int,
    val name: String,
    val headUrl: String?
) {
    fun toAppUserVo(): AppUserVo {
        return AppUserVo(
            id = userId,
            name = name,
            avatar = headUrl
        )
    }

    fun toFriendVo(): FriListVos {
        return FriListVos(
            id = userId,
            friendName = name,
            headUrl = headUrl
        )
    }
}