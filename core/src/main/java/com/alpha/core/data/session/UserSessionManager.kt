package com.alpha.core.data.session

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSessionManager @Inject constructor(private val context: Context) {

    companion object {
        val USER_LOGGED_IN = booleanPreferencesKey("user_logged_in")
        val USER_EMAIL = stringPreferencesKey("user_email")
    }

    val userLoggedIn: Flow<Boolean> = context.userSessionDataStore.data.map {
        it[USER_LOGGED_IN] ?: false
    }

    val userEmail: Flow<String?> = context.userSessionDataStore.data.map {
        it[USER_EMAIL]
    }

    suspend fun saveSession(isLoggedIn: Boolean, email: String) {
        context.userSessionDataStore.edit { preferences ->
            preferences[USER_LOGGED_IN] = isLoggedIn
            preferences[USER_EMAIL] = email
        }
    }
}

