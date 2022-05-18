package com.galaxytechno.chat.model.dto

import com.galaxytechno.chat.model.response.BaseResponse

data class QuestionsResponse(
    override val status: String,
    override val messageCode: Int,
) : BaseResponse<SecurityQuest>()

data class SecurityQuest(
    val securityQuesList_A: List<SecurityQuestObj>,
    val securityQuesList_B: List<SecurityQuestObj>,
    val securityQuesList_C: List<SecurityQuestObj>
)

data class SecurityQuestObj(
    val questionType: Int = 0,
    val questionId: Int = 0,
    val question: String = ""
)