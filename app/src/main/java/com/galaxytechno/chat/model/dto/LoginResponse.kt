package com.galaxytechno.chat.model.dto

import com.galaxytechno.chat.model.entity.UserEntity
import com.galaxytechno.chat.model.response.BaseResponse
import org.junit.experimental.theories.FromDataPoints

data class LoginResponse(
    override val status: String,
    override val messageCode: Int,
) : BaseResponse<ProfileInfoData>()

data class ProfileInfoData(
    val profileInfo: ProfileInfo,
    val accessToken: String,
    val refreshToken: String
)

data class ProfileInfo(
    val userId: Long = -1,
    val name: String = "",
    val headUrl: String? = "",
    val coverImgUrl: String? = "",
    val bio: String? = "",
    val birthDate: String? = "",
    val gender: Int? = -1,
    val countryId: Int = -1,
    val mobile: String = "",
    val email: String? = "",
    val accountId: String = "",
    val locale: Int = 0,
    val isTwoFactor: Boolean = false,
    val isProfileLock: Boolean = false,
    val totalFriends: Int = 0,
    val friendStatus: Int = -1,
    val isBeingBlock: Boolean = false,
    val isBlock: Boolean = false
) {
    fun toEntity(): UserEntity {
        return UserEntity(
            userId = userId,
            name = name,
            headUrl = headUrl ?: "",
            coverImgUrl = coverImgUrl ?: "",
            bio = bio ?: "",
            birthDate = birthDate ?: "",
            gender = gender ?: -1,
            countryId = countryId,
            mobile = mobile,
            email = email ?: "",
            accountId = accountId,
            locale = locale,
            isTwoFactor = isTwoFactor,
            isProfileLock = isProfileLock,
            totalFriends = totalFriends,
            friendStatus = friendStatus,
            isBeingBlock = isBeingBlock,
            isBlock = isBlock
        )
    }
}



