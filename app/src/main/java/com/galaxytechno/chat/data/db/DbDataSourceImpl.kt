package com.galaxytechno.chat.data.db

import com.galaxytechno.chat.model.entity.UserEntity
import javax.inject.Inject

open class DbDataSourceImpl @Inject constructor(
    private val db : UserDatabase
) : DbDataSource {
    override suspend fun saveUser(userEntity: UserEntity) {
        db.getDao().insertUser(userEntity)
    }

    override suspend fun getUser(): UserEntity {
        return db.getDao().retrieveUser()
    }

    override suspend fun deleteUser() {
        return db.getDao().deleteUser()
    }
}