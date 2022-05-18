package com.galaxytechno.chat.model.request

class VerifyConfirmedQuestionRequest(
    val countryId: Int,
    val mobile: String,
    val locale : Int,
    val answerLists: HashSet<ConfirmedQuestionsAndAnswers>
)

data class ConfirmedQuestionsAndAnswers(
    val id: Int,
    val questionId: Int,
    val answer: String
)



