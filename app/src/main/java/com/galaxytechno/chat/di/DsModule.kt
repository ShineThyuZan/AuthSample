package com.galaxytechno.chat.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.galaxytechno.chat.common.Constant
import com.galaxytechno.chat.data.ds.DsDataSource
import com.galaxytechno.chat.data.ds.DsDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DsModule {

    @Provides
    @Singleton
    fun provideDsDataSource(ds: DataStore<Preferences>): DsDataSource {
        return DsDataSourceImpl(ds)
    }

    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile(Constant.DS_NAME)
            }
        )
    }
}