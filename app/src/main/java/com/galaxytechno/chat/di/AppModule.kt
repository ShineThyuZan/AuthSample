package com.galaxytechno.chat.di

import android.content.Context
import com.galaxytechno.chat.common.InternetChecker
import com.galaxytechno.chat.util.NotificationUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(ActivityComponent::class)
object AppModule {
    @Provides
    @ActivityScoped
    fun providesNetwork(
        @ApplicationContext context: Context,
        @Qualifier.Io io: CoroutineDispatcher
    ): InternetChecker {
        return InternetChecker(context, io)
    }

    @Provides
    @ActivityScoped
    fun providesNotification(
        @ApplicationContext context: Context
    ): NotificationUtil {
        return NotificationUtil((context))
    }

}