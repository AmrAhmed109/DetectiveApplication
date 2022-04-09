package com.example.detectiveapplication.repository

import com.example.detectiveapplication.datastore.UserDataStore
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class DataStoreRepository @Inject constructor(
    private val userDataStore: UserDataStore,
) {

    fun saveToken(value: String) {
        userDataStore.setToken(value)
    }

    val readToken: String = userDataStore.getToken()
}