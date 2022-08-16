package com.patmore.android.core.utility

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

    fun saveTwitterUserAccessToken(accessToken: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString("twitterUserAccessToken", accessToken)
        editor.apply()
    }

    fun getTwitterUserAccessToken(): String? {
        return sharedPref.getString("twitterUserAccessToken", null)
    }

    fun saveTwitterUserRefreshToken(accessToken: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString("twitterUserRefreshToken", accessToken)
        editor.apply()
    }

    fun getTwitterUserRefreshToken(): String? {
        return sharedPref.getString("twitterUserRefreshToken", null)
    }

    fun saveTwitterUserId(userId: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString("twitterUserId", userId)
        editor.apply()
    }

    fun getTwitterUserId(): String? {
        return sharedPref.getString("twitterUserId", null)
    }

    fun saveTokenExpiration(time: Long) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        val t = System.currentTimeMillis() + time
        editor.putLong("tokenExpiration", t)
        editor.apply()
    }

    fun getTokenExpiration(): Long {
        return sharedPref.getLong("tokenExpiration", 1660415199624L)
    }

    fun saveOAuthToken(token: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString("oauthToken", token)
        editor.apply()
    }

    fun getOauthToken(): String? {
        return sharedPref.getString("oauthToken", null)
    }

    fun saveOAuthSecret(token: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString("oauthSecret", token)
        editor.apply()
    }

    fun getOauthSecret(): String? {
        return sharedPref.getString("oauthSecret", null)
    }
}
