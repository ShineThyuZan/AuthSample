package com.galaxytechno.chat.domain

import com.galaxytechno.chat.model.dto.Contacts
import com.galaxytechno.chat.model.dto.FriendAddResponse
import com.galaxytechno.chat.model.dto.ProfileInfoResponse
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import com.galaxytechno.chat.util.DataResource
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface ContactsRepository {
    suspend fun getContacts(): Flow<DataResource<List<Contacts>>>


}