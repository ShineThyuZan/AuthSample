package com.galaxytechno.chat.domain

import com.galaxytechno.chat.common.RemoteDataSource
import com.galaxytechno.chat.data.ds.DsDataSource
import com.galaxytechno.chat.data.remote.ApiService
import com.galaxytechno.chat.di.Qualifier
import com.galaxytechno.chat.model.dto.LanguagesResponse
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val ds: DsDataSource,
    @Qualifier.Io private val io: CoroutineDispatcher,
) : AppRepository, RemoteDataSource() {

    //DS
    override suspend fun getLocaleStatus(): Flow<Int> {
        return ds.pullLocaleStatus()
    }

    override suspend fun storeAuthState(isLoggedIn: Boolean) {
        withContext(io) {
            ds.putAuthState(isLoggedIn)
        }
    }

    override suspend fun storeLanguageChosenState(isSelected: Boolean) {
        withContext(io) {
            ds.putLanguageChosenState(isSelected)
        }
    }

    override suspend fun storeLocaleStatus(locale: Int) {
        withContext(io) {
            ds.putLocaleStatus(locale)
        }
    }

    override suspend fun getAuthState(): Flow<Boolean> {
        return ds.pullAuthState()
    }


    override suspend fun getLanguageChosenState(): Flow<Boolean> {
        return ds.pullLanguageChosenState()
    }

    override suspend fun removeAuthState() {
        withContext(io){
            ds.removeAuthState()
        }
    }



    override suspend fun getLanguages(): Flow<RemoteEvent<LanguagesResponse>> {
        return flow {
            emit(
                safeApiCall {
                    api.getLanguages()
                }
            )
        }.flowOn(io)
    }



}
