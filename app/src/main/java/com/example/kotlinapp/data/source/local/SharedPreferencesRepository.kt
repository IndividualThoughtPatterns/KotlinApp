package com.example.kotlinapp.data.source.local


import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class SharedPreferencesRepository(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    private val CACHING_MODE = "caching_mode"

    val cachingModeFlow: Flow<Boolean> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
            if (key == CACHING_MODE) {
                trySend(prefs.getBoolean(CACHING_MODE, true))
            }
        }

        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

        trySend(sharedPreferences.getBoolean(CACHING_MODE, true))

        awaitClose {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    fun setCachingMode(mode: Boolean) {
        sharedPreferences.edit { putBoolean(CACHING_MODE, mode) }
    }
}