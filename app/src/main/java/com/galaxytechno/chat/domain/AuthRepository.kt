package com.galaxytechno.chat.domain

import com.galaxytechno.chat.model.dto.*
import com.galaxytechno.chat.model.request.ConfirmedQuestionsAndAnswers
import com.galaxytechno.chat.model.request.QuestAndAnsObj
import com.galaxytechno.chat.model.dto.QuestionsResponse
import com.galaxytechno.chat.model.response.VerifiedResponse
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    //Master
    suspend fun getCountries(): Flow<RemoteEvent<CountriesResponse>>
    suspend fun getQuestions(): Flow<RemoteEvent<QuestionsResponse>>

    suspend fun login(
        countryId: Int,
        mobile: String,
        password: String,
    ): Flow<RemoteEvent<LoginResponse>>

    suspend fun signUp(
        countryId: Int ,
        name: String,
        mobile: String,
        password: String,
        securityQuestionUsage: HashSet<QuestAndAnsObj>
    ): Flow<RemoteEvent<LoginResponse>>

    suspend fun requestOtp(
        countryId: Int,
        mobile: String,
    ): Flow<RemoteEvent<GetOtpResponse>>

    suspend fun validateOTP(
        countryId: Int,
        mobile: String,
        otpCode: String,
    ): Flow<RemoteEvent<ValidateOtpResponse>>

    suspend fun checkAccount(
        countryId: Int,
        mobile: String
    ): Flow<RemoteEvent<CheckAccountByMobileResponse>>

    suspend fun getConfirmedQuestions(
        countryId: Int,
        mobile: String
    ): Flow<RemoteEvent<GetConfirmedQuestionsResponse>>

    suspend fun verifyConfirmedQuestions(
        countryId: Int,
        mobile: String,
        answerLists: HashSet<ConfirmedQuestionsAndAnswers>
    ): Flow<RemoteEvent<VerifiedResponse>>

    suspend fun resetPassword(
        countryId: Int,
        mobile: String,
        newPassword: String,
        confirmPassword: String
    ): Flow<RemoteEvent<VerifiedResponse>>

    suspend fun storeLocaleStatus(locale: Int)
}