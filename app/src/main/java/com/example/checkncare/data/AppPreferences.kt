package com.example.checkncare.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Single DataStore instance per app
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_prefs")

object AppPreferences {
    private val KEY_DARK_MODE = booleanPreferencesKey("dark_mode")

    fun isDarkMode(context: Context): Flow<Boolean> =
        context.dataStore.data.map { prefs -> prefs[KEY_DARK_MODE] ?: false }

    suspend fun setDarkMode(context: Context, isDark: Boolean) {
        context.dataStore.edit { prefs -> prefs[KEY_DARK_MODE] = isDark }
    }
}
