package com.example.detectiveapplication.datastore

import android.content.Context
import com.example.detectiveapplication.datastore.utils.KEY
import com.example.detectiveapplication.datastore.utils.Language
import com.example.detectiveapplication.datastore.utils.SharedPreferences
import com.example.detectiveapplication.datastore.utils.Value.NULL_STRING

class UserDataStore(context: Context) {

    private val userSharedPreferences =
        context.getSharedPreferences(
            SharedPreferences.USER_SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )

    fun setToken(token: String) =
        userSharedPreferences.edit().putString(KEY.TOKEN, token).apply()

    fun getToken(): String =
        userSharedPreferences.getString(KEY.TOKEN, NULL_STRING)!!

    fun setLanguage(language: Language) {
        when (language) {
            is Language.Arabic -> {
                userSharedPreferences.edit().putString(KEY.LANGUAGE, language.key).apply()
            }

            is Language.English -> {
                userSharedPreferences.edit().putString(KEY.LANGUAGE, language.key).apply()
            }
        }
    }

    fun getLanguage(): String =
        userSharedPreferences.getString(KEY.LANGUAGE, NULL_STRING)!!
}

