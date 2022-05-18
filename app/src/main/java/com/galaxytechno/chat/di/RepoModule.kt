package com.galaxytechno.chat.di

import android.content.Context
import com.galaxytechno.chat.common.Endpoint
import com.galaxytechno.chat.data.db.DbDataSource
import com.galaxytechno.chat.data.ds.DsDataSource
import com.galaxytechno.chat.data.remote.ApiService
import com.galaxytechno.chat.domain.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import java.net.URISyntaxException
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepoModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        apiDataSource: ApiService,
        dbDataSource: DbDataSource,
        dsDataSource: DsDataSource,
        @Qualifier.Io io: CoroutineDispatcher,
        @Qualifier.RepoScope repoScope: CoroutineScope,
        apiService: ApiService
    ): UserRepository {
        return UserRepositoryImpl(
            api = apiDataSource,
            db = dbDataSource,
            ds = dsDataSource,
            io = io,
            repoScope = repoScope,
            apiService = apiService
        )
    }

    @Provides
    @Singleton
    fun provideAppRepository(
        apiDataSource: ApiService,
        dsDataSource: DsDataSource,
        @Qualifier.Io io: CoroutineDispatcher,
    ): AppRepository {
        return AppRepositoryImpl(
            api = apiDataSource,
            ds = dsDataSource,
            io = io
        )
    }
    @Provides
    @Singleton
    fun provideContactsRepository(
        apiDataSource: ApiService,
        dsDataSource: DsDataSource,
        @ApplicationContext context: Context,
        @Qualifier.Io io: CoroutineDispatcher,
        @Qualifier.RepoScope repoScope: CoroutineScope,
    ): ContactsRepository {
        return ContactsRepositoryImpl(
            context = context,
            io = io,
            repoScope = repoScope,
            ds = dsDataSource,
            api = apiDataSource
        )
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        apiDataSource: ApiService,
        dsDataSource: DsDataSource,
        @Qualifier.Io io: CoroutineDispatcher,
        @Qualifier.RepoScope repoScope: CoroutineScope,
    ): AuthRepository {
        return AuthRepositoryImpl(
            api = apiDataSource,
            ds = dsDataSource,
            io = io,
            repoScope = repoScope,
        )
    }

    @Provides
    @Singleton
    fun provideSocket(): Socket {
        val socket: Socket
        try {
            socket = IO.socket(Endpoint.SOCKET_HOST) //this is your socket server
        } catch (e: URISyntaxException) {
            throw URISyntaxException(e.message, "Wrong URI")
        } catch (e: Exception) {
            throw Exception(e.message)
        }
        return socket
    }
}