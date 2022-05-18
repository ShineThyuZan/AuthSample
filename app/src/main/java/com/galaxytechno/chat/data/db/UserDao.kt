package com.galaxytechno.chat.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.galaxytechno.chat.common.Constant
import com.galaxytechno.chat.model.entity.UserEntity

@Dao
interface UserDao {
    //todo : abstraction about Data Access Object
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity)

    @Query("SELECT * FROM ${Constant.USER_TABLE}")
    suspend fun retrieveUser() : UserEntity

    @Query("DELETE FROM ${Constant.USER_TABLE}")
    suspend fun deleteUser()

}