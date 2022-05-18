package com.galaxytechno.chat.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.galaxytechno.chat.model.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 5,
    exportSchema = false
)
abstract class  UserDatabase : RoomDatabase() {
    abstract fun getDao(): UserDao
}