package com.example.detectiveapplication.repository

import com.example.detectiveapplication.service.api.AuthApi
import com.example.detectiveapplication.dto.response.UserLoginResponse
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Response
import javax.inject.Inject


@ViewModelScoped
class AuthRepository @Inject constructor(private val authApi: AuthApi) {

    suspend fun login(map: Map<String, String>): Response<UserLoginResponse> {
        return authApi.userLogin(map)
    }

}