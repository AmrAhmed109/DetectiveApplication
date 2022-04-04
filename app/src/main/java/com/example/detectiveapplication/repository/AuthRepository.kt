package com.example.detectiveapplication.repository

import com.example.detectiveapplication.dto.auth_response.forget_password.ForgetPasswordResponse
import com.example.detectiveapplication.service.api.AuthApi
import com.example.detectiveapplication.dto.auth_response.login.UserLoginResponse
import com.example.detectiveapplication.dto.auth_response.logout.UserLogoutResponse
import com.example.detectiveapplication.dto.auth_response.profile_data.UserProfileInfo
import com.example.detectiveapplication.dto.auth_response.registration.UserRegistrationResponse
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Response
import javax.inject.Inject


@ViewModelScoped
class AuthRepository @Inject constructor(private val authApi: AuthApi) {

    suspend fun login(map: Map<String, String>): Response<UserLoginResponse> {
        return authApi.userLogin(map)
    }

    suspend fun registration(map: Map<String, String>): Response<UserRegistrationResponse> {
        return authApi.userRegistration(map)
    }

    suspend fun forgetPassword(map: Map<String, String>): Response<ForgetPasswordResponse> {
        return authApi.userForgetPassword(map)
    }

    suspend fun logout(token:String): Response<UserLogoutResponse> {
        return authApi.userLogout(token)
    }

    suspend fun getUserData(token:String): Response<UserProfileInfo> {
        return authApi.getUserInfo(token)
    }

}