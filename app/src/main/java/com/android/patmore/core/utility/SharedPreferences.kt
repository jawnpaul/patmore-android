package com.android.patmore.core.utility

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferences @Inject constructor(@ApplicationContext context: Context) {
    private val prefsName = "patmore_app_prefs"
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)

    fun saveIsFirstInstall(isFirstInstall: Boolean) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putBoolean("isFirstInstall", isFirstInstall)
        editor.apply()
    }

    fun getIsFirstInstall(): Boolean {
        return sharedPref.getBoolean("isFirstInstall", true)
    }

    fun saveAccessToken(accessToken: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString("accessToken", accessToken)
        editor.apply()
    }

    fun getAccessToken(): String? {
        return sharedPref.getString("accessToken", null)
    }

    fun saveUserCategories(categories: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString("userCategories", categories.trim())
        editor.apply()
    }

    fun getUserCategories(): String? {
        return sharedPref.getString("userCategories", null)
    }
}
