package com.galaxytechno.chat.data.ds

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

open class DsDataSourceImpl @Inject constructor(
    private val ds: DataStore<Preferences>
) : DsDataSource {

    companion object {
        val AUTH_STATE = booleanPreferencesKey("com.galaxy_techno.auth_state")
        val LANG_SELECT_STATUS = booleanPreferencesKey("com.galaxy_techno.language.status")
        val ACCESS_TOKEN = stringPreferencesKey("com.galaxy_techno.access_token")
        val REFRESH_TOKEN = stringPreferencesKey("com.galaxy_techno.refresh_token")
        val LOCALE_STATUS = intPreferencesKey("com.galaxy_techno.locale.status")
    }

    override suspend fun putAuthState(isLoggedIn: Boolean) {
        ds.edit {
            it[AUTH_STATE] = isLoggedIn
        }
    }

    override suspend fun pullAuthState(): Flow<Boolean> {
        return ds.data
            .catch { exception ->
                if (exception is IOException) emit(emptyPreferences()) else throw exception

            }.map {
                it[AUTH_STATE] ?: false
            }
    }

    override suspend fun putAccessToken(token: String) {
        ds.edit {
            it[ACCESS_TOKEN] = token
        }
    }

    override suspend fun pullAccessToken(): Flow<String> {
        return ds.data.catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }.map {
            it[ACCESS_TOKEN] ?: "empty_access_token"
        }
    }

    override suspend fun putRefreshToken(token: String) {
        ds.edit {
            it[REFRESH_TOKEN] = token
        }
    }

    override suspend fun pullRefreshToken(): Flow<String> {
        return ds.data.catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }.map {
            it[REFRESH_TOKEN] ?: "empty_refresh_token"
        }
    }


    override suspend fun putLocaleStatus(status: Int) {
        ds.edit {
            it[LOCALE_STATUS] = status
        }
    }

    override suspend fun pullLocaleStatus(): Flow<Int> {
        return ds.data.catch {
                exception -> if (exception is IOException) emit(emptyPreferences())
        else throw exception
        }
            .map {
                it[LOCALE_STATUS] ?: 0 }
        //todo change locale default value 0 to -1 later, this is for testing purpose
    }

    override suspend fun putLanguageChosenState(isSelect: Boolean) {
        ds.edit {
            it[LANG_SELECT_STATUS] = isSelect
        }
    }

    override suspend fun pullLanguageChosenState(): Flow<Boolean> {
        return ds.data.catch { exception -> if (exception is IOException) emit(emptyPreferences()) else throw exception }
            .map { it[LANG_SELECT_STATUS] ?: false }
    }

    override suspend fun clearDs() {
        ds.edit {
            it.clear()
        }
    }

    override suspend fun removeAuthState() {
        ds.edit {
            it.remove(AUTH_STATE)
        }
    }

    override suspend fun removeAccessToken() {
        ds.edit {
            it.remove(ACCESS_TOKEN)
        }
    }

    override suspend fun removeRefreshToken() {
        ds.edit {
            it.remove(REFRESH_TOKEN)
        }
    }
}