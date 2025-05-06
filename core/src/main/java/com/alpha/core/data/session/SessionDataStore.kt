package com.alpha.core.data.session

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.userSessionDataStore: androidx.datastore.core.DataStore<Preferences> by preferencesDataStore(
    name = "user_session"
)
