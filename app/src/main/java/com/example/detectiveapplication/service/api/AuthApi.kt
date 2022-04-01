package com.example.detectiveapplication.service.api

import com.example.detectiveapplication.dto.auth_response.login.UserLoginResponse
import com.example.detectiveapplication.dto.auth_response.registration.UserRegistrationResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {


    /**
     * User Parameter
     * email - password
     * */
    @POST("user/login")
    @Headers("Content-Type: application/json")
    suspend fun userLogin(
        @Body body: Map<String, String>,
    ):Response<UserLoginResponse>


    /**
     * User Parameter
     * email* - password* - password_confirmation* -image(not required) - name*
     * */
    @POST("user/register")
    @Headers("Content-Type: application/json")
    suspend fun userRegistration(
        @Body body: Map<String, String>
    ):Response<UserRegistrationResponse>
//        @Part image:MultipartBody.Part? = null



    @POST("logout-user")
    @Headers("Content-Type: application/json")
    suspend fun userLogout(
        @Header("Authorization") token: String
    ):Response<UserLoginResponse>

    @POST("user/me")
    @Headers("Content-Type: application/json")
    suspend fun getMyUserData(
        @Header("Authorization") token: String
    ):Response<UserLoginResponse>
}