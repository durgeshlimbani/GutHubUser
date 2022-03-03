package com.limsbro.guthubuser.helper

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Durgesh Patel on 01,March,2022.
 */
class PrefsHelper(private val context: Context?) {

    private val prefs: SharedPreferences? by lazy {
        context?.getSharedPreferences(
            "GitPrefs",
            Context.MODE_PRIVATE
        )
    }

    fun saveUsername(userName: String) {
        prefs?.edit()?.putString("userName", userName)?.apply()
    }

    fun getUserName() = prefs?.getString("userName", "") ?: ""

    fun saveUserId(userID: Int) {
        prefs?.edit()?.putInt("userID", userID)?.apply()
    }

    fun getUserId() = prefs?.getInt("userID", 0) ?: 0

}