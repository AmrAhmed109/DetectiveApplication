package com.example.detectiveapplication.repository

import android.content.Context
import android.util.Log
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okio.IOException
import javax.inject.Inject

@ViewModelScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferenceKeys {
        val token = preferencesKey<String>("token")

    }
    private val dataStore: DataStore<Preferences> = context.createDataStore(name = "token")

    suspend fun saveToken(value : String){
        dataStore.edit { preference ->
            preference[PreferenceKeys.token] = value
            Log.d("DataStore", "save token : $value")
        }
    }

    val readToken: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.d("DataStore", exception.message.toString())
                emit(emptyPreferences())
            } else {
                throw exception
            }

        }.map { preference ->
            val getToken : String = preference[PreferenceKeys.token] ?: ""
            Log.d("DataStore", "read token : $getToken")
            getToken
        }


}