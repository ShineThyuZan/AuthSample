package com.galaxytechno.chat.model.dto

class FriContactCheckResponse(

    val status: String,
    val messageCode: Int,
    val message: String,
    val data: FriendStatusInfo,
    val error : String
)

data class FriendStatusInfo(
    val appUserInfos: List<ContactFriObj>
)

data class ContactFriObj(
    val userId: Long,
    val mobile: String,
    val name: String,
    val friendStatus: Int,
    val headUrl: String
)