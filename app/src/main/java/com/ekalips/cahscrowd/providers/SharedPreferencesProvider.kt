package com.ekalips.cahscrowd.providers

import android.content.Context
import android.content.Context.MODE_PRIVATE
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesProvider @Inject constructor(private val context: Context) {

    fun getNamedPreferences(name: String) = context.getSharedPreferences(name, MODE_PRIVATE)
    fun getDefaultPreferences() = getNamedPreferences(DEFAULT_PREFS_NAME)


    companion object {
        private const val DEFAULT_PREFS_NAME = "com.ekalips.cahscrowd.DEFAULT"
    }
}