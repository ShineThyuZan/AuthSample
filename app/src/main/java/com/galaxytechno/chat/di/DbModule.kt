package com.galaxytechno.chat.di

import android.content.Context
import androidx.room.Room
import com.galaxytechno.chat.common.Constant
import com.galaxytechno.chat.data.db.DbDataSource
import com.galaxytechno.chat.data.db.DbDataSourceImpl
import com.galaxytechno.chat.data.db.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DbModule {
    @Provides
    @Singleton
    fun provideDbDataSource(db: UserDatabase): DbDataSource {
        return DbDataSourceImpl(db)
    }

    @Provides
    @Singleton
    fun provideUserDb(
        @ApplicationContext context: Context
    ): UserDatabase {
        return Room.databaseBuilder(
            context,
            UserDatabase::class.java,
            Constant.DB_NAME
        ).fallbackToDestructiveMigration().build()
    }

}