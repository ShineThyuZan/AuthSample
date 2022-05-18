package com.galaxytechno.chat.data.remote

import com.galaxytechno.chat.common.Endpoint
import com.galaxytechno.chat.di.Qualifier
import com.galaxytechno.chat.domain.UserRepository
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    @Qualifier.RepoScope private val repoScope: CoroutineScope
) : Authenticator {

    companion object {
        private const val FAIL_TOKEN_CODE = 401
        private const val SUCCESS_TOKEN_CODE = 200

    }

    @Inject
    lateinit var repo: UserRepository

    override fun authenticate(
        route: Route?,
        response: Response
    ): Request {

        Timber.tag("k1for0").d(response.toString())

        var accessToken: String? = null
        var refreshToken: String? = null

        repoScope.launch {
            repo.getAccessToken().collect {
                accessToken = it
            }
            repo.getRefreshToken().collect {
                refreshToken = it
            }

            refreshToken?.let { refreshToken ->

                repo.fetchRefreshToken(refreshToken).collect {

                    val tokenData = it.data?.data

                    when (it) {
                        is RemoteEvent.LoadingEvent -> {
                        }
                        is RemoteEvent.ErrorEvent -> {
                        }
                        is RemoteEvent.SuccessEvent -> {
                            repo.saveAccessToken(tokenData?.accessToken!!)
                            repo.saveRefreshToken(tokenData.refreshToken)
                        }
                    }
                }
            }
        }

        return when (response.code) {

            SUCCESS_TOKEN_CODE -> {
                response
                    .request
                    .newBuilder()
                    .header(Endpoint.AUTHORIZATION, "Bearer $accessToken")
                    .build()
            }

            FAIL_TOKEN_CODE -> {
                response
                    .request
                    .newBuilder()
                    .header(Endpoint.AUTHORIZATION, "Bearer $accessToken")
                    .build()
            }

            else -> {
                response
                    .request
                    .newBuilder()
                    .header(Endpoint.AUTHORIZATION, "Bearer $accessToken")
                    .build()
            }
        }

    }

}