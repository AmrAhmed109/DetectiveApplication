package com.example.detectiveapplication.service.api

import com.example.detectiveapplication.dto.response.UserLoginResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {

//    @POST("user/login")
//    suspend fun userLogin(
//        @Field("email") email:String,
//        @Field("password") password:String
//    ):Response<UserLoginResponse>


    @POST("user/login")
    @Headers("Content-Type: application/json")
    suspend fun userLogin(
        @Body body: Map<String, String>,
        @Part image:MultipartBody.Part
    ):Response<UserLoginResponse>


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