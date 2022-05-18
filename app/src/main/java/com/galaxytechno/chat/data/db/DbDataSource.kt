package com.galaxytechno.chat.data.db

import com.galaxytechno.chat.model.entity.UserEntity

interface DbDataSource {
    suspend fun saveUser(userEntity: UserEntity)
    suspend fun getUser(): UserEntity
    suspend fun deleteUser()
}