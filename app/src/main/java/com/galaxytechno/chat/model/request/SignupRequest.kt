package com.galaxytechno.chat.model.request

class SignupRequest(
    val countryId: Int = 0,
    val name: String = "",
    val mobile: String = "",
    val password: String = "",
    val uuid : String,
    val isMobile : Boolean,
    val locale : Int,
    val securityQuestionUsage: HashSet<QuestAndAnsObj>?
)

data class QuestAndAnsObj(
    val questionId: Int = 0,
    val questionType: Int = 0,
    val question: String = "",
    val answer: String = ""
)