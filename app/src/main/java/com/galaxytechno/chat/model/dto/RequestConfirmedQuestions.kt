package com.galaxytechno.chat.model.dto

import com.galaxytechno.chat.model.response.BaseResponse

class GetConfirmedQuestionsResponse(
    override val status: String,
    override val messageCode: Int,
) : BaseResponse<QuestionAndAnsList>()

data class QuestionAndAnsList(
    val questionAndAnsList: MutableList<FwdPwdQuestObj>
)

data class FwdPwdQuestObj(
    val id: Int = 0,
    val questionType: Int = 0,
    val questionId: Int = 0,
    val question: String = ""
)