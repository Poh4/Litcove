package com.litcove.litcove.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore by preferencesDataStore(name = "settings")

@Singleton
class PreferenceRepository @Inject constructor(@ApplicationContext private val context: Context) {

    val themeSetting: Flow<Boolean?> = context.dataStore.data.map { preferences ->
        val themeKey = booleanPreferencesKey("theme_key")
        preferences[themeKey]
    }

    suspend fun saveThemeSetting(isDarkMode: Boolean) {
        val themeKey = booleanPreferencesKey("theme_key")
        context.dataStore.edit { preferences ->
            preferences[themeKey] = isDarkMode
        }
    }

    suspend fun clearThemeSetting() {
        val themeKey = booleanPreferencesKey("theme_key")
        context.dataStore.edit { preferences ->
            preferences.remove(themeKey)
        }
    }
}