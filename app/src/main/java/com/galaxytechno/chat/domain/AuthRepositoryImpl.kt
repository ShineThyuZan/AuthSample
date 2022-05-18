package com.galaxytechno.chat.domain

import com.galaxytechno.chat.common.RemoteDataSource
import com.galaxytechno.chat.data.ds.DsDataSource
import com.galaxytechno.chat.data.remote.ApiService
import com.galaxytechno.chat.di.Qualifier
import com.galaxytechno.chat.model.dto.*
import com.galaxytechno.chat.model.request.*
import com.galaxytechno.chat.model.response.VerifiedResponse
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val ds: DsDataSource,
    @Qualifier.Io private val io: CoroutineDispatcher,
    @Qualifier.RepoScope private val repoScope: CoroutineScope,
) : AuthRepository, RemoteDataSource() {

    private var repoLocale: Int? = null

    init {
        repoScope.launch {
            repoLocale = getLocaleFromRepo()
        }
    }

    private suspend fun getLocaleFromRepo(): Int {
        return ds.pullLocaleStatus().first()
    }

    //API
    override suspend fun getCountries(): Flow<RemoteEvent<CountriesResponse>> {
        return flow {
            emit(
                safeApiCall {
                    api.getCountries(repoLocale ?: 0)
                }
            )
        }
    }

    override suspend fun getQuestions(): Flow<RemoteEvent<QuestionsResponse>> {
        return flow {
            emit(
                safeApiCall {
                    api.getQuestions(repoLocale ?: 0)
                }
            )
        }.flowOn(io)
    }

    override suspend fun login(
        countryId: Int,
        mobile: String,
        password: String
    ): Flow<RemoteEvent<LoginResponse>> {
        return flow {
            emit(
                safeApiCall {
                    api.login(
                        countryId = countryId,
                        mobile = mobile,
                        password = password,
                        uuid = UUID.randomUUID().toString(),
                        isMobile = true,
                        locale = repoLocale ?: 0
                    )
                }
            )
        }.flowOn(io)
    }

    override suspend fun signUp(
        countryId: Int,
        name: String,
        mobile: String,
        password: String,
        securityQuestionUsage: HashSet<QuestAndAnsObj>
    ): Flow<RemoteEvent<LoginResponse>> {
        val request = SignupRequest(
            countryId = countryId,
            name = name,
            mobile = mobile,
            password = password,
            uuid = UUID.randomUUID().toString(),
            isMobile = true,
            securityQuestionUsage = securityQuestionUsage,
            locale = repoLocale ?: 0
        )
        return flow<RemoteEvent<LoginResponse>> {
            emit(
                safeApiCall {
                    api.signup(request)
                }
            )
        }.flowOn(io)
    }

    override suspend fun requestOtp(
        countryId: Int,
        mobile: String
    ): Flow<RemoteEvent<GetOtpResponse>> {
        return flow {
            emit(
                safeApiCall {
                    api.requestOtp(
                        mobile = mobile,
                        countryId = countryId,
                        locale = repoLocale ?: 0
                    )
                }
            )
        }.flowOn(io)
    }

    override suspend fun validateOTP(
        countryId: Int,
        mobile: String,
        otpCode: String
    ): Flow<RemoteEvent<ValidateOtpResponse>> {
        return flow {
            emit(
                safeApiCall {
                    api.validateOtp(
                        countryId = countryId,
                        mobileNumber = mobile,
                        otpCode = otpCode,
                        locale = repoLocale ?: 0
                    )
                }
            )
        }.flowOn(io)
    }

    override suspend fun checkAccount(
        countryId: Int,
        mobile: String
    ): Flow<RemoteEvent<CheckAccountByMobileResponse>> {
        return flow {
            emit(
                safeApiCall {
                    api.checkAccountStatus(
                        countryId = countryId,
                        mobile = mobile,
                        locale = repoLocale ?: 0
                    )
                }
            )
        }.flowOn(io)
    }

    override suspend fun getConfirmedQuestions(
        countryId: Int,
        mobile: String
    ): Flow<RemoteEvent<GetConfirmedQuestionsResponse>> {
        return flow {
            emit(
                safeApiCall {
                    api.requestConfirmedQuestions(
                        countryId = countryId,
                        mobile = mobile,
                        locale = repoLocale ?: 0
                    )
                }
            )
        }.flowOn(io)
    }

    override suspend fun verifyConfirmedQuestions(
        countryId: Int,
        mobile: String,
        answerLists: HashSet<ConfirmedQuestionsAndAnswers>
    ): Flow<RemoteEvent<VerifiedResponse>> {
        val request = VerifyConfirmedQuestionRequest(
            countryId = countryId,
            mobile = mobile,
            answerLists = answerLists,
            locale = repoLocale ?: 0
        )
        return flow<RemoteEvent<VerifiedResponse>> {
            emit(
                safeApiCall {
                    api.validateConfirmedQuestions(request)
                }
            )
        }.flowOn(io)
    }

    override suspend fun resetPassword(
        countryId: Int,
        mobile: String,
        newPassword: String,
        confirmPassword: String
    ): Flow<RemoteEvent<VerifiedResponse>> {
        val request = PasswordResetRequest(
            countryId = countryId,
            mobile = mobile,
            newPassword = newPassword,
            confirmPassword = confirmPassword,
            locale = repoLocale ?: 0
        )
        return flow {
            emit(
                safeApiCall {
                    api.resetPassword(request)
                }
            )
        }
    }

    override suspend fun storeLocaleStatus(locale: Int) {
        withContext(io) {
            ds.putLocaleStatus(locale)
        }
    }
}