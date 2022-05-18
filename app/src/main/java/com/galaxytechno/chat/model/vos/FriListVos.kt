package com.galaxytechno.chat.model.vos

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FriListVos(
    val id: Int? = 0,
    val friendId: Long? = 0,
    val friendName: String? = "",
    val headUrl: String? = "",
    val isChecked: Boolean? = false
) : Parcelable


@Parcelize
data class AppUserVo(
    val id: Int = -1,
    val name: String = "",
    val avatar: String? = ""
) : Parcelable {
    fun toVo(): FriListVos {
        return FriListVos(
            id = id,
            friendName = name,
            headUrl = avatar
        )
    }
}
