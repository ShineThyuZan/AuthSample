package com.galaxytechno.chat.domain

import com.galaxytechno.chat.model.CountryListResponse
import com.galaxytechno.chat.model.dto.LanguagesResponse
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import kotlinx.coroutines.flow.Flow

interface AppRepository {

    //DS
    suspend fun storeAuthState(isLoggedIn: Boolean)

    suspend fun storeLanguageChosenState(isSelected: Boolean)

    suspend fun storeLocaleStatus(locale: Int)

    suspend fun getAuthState(): Flow<Boolean>

    suspend fun getLanguageChosenState(): Flow<Boolean>

    suspend fun getLocaleStatus(): Flow<Int>

    //API ( master data )
    suspend fun getLanguages(): Flow<RemoteEvent<LanguagesResponse>>

    suspend fun removeAuthState()

}