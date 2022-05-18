package com.galaxytechno.chat.model.dto

class CreateGroupResponse(
    val status: String,
    val messageCode: Int,
    val message: String,
    val data: GroupChatMemberVOs
)
data class GroupChatMemberVOs(
    val groupId: Int,
    val groupName: String,
    val description: String,
    val groupLink: String,
    val headUrl: String,
    val blockingCount: Int,
    val isGroup: Boolean
)