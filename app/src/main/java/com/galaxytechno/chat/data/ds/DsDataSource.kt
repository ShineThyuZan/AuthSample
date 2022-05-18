package com.galaxytechno.chat.data.ds

import kotlinx.coroutines.flow.Flow

interface DsDataSource {

    //for APP
    suspend fun putAuthState(isLoggedIn: Boolean)
    suspend fun pullAuthState(): Flow<Boolean>
    suspend fun putLanguageChosenState(isSelect: Boolean)
    suspend fun pullLanguageChosenState(): Flow<Boolean>
    suspend fun putLocaleStatus(status: Int)
    suspend fun pullLocaleStatus(): Flow<Int>

    //for User
    suspend fun putAccessToken(token: String)
    suspend fun pullAccessToken(): Flow<String>
    suspend fun putRefreshToken(token: String)
    suspend fun pullRefreshToken(): Flow<String>

    //for logout
    suspend fun removeAuthState()
    suspend fun removeAccessToken()
    suspend fun removeRefreshToken()

    //for clear all
    suspend fun clearDs()
}