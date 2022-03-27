package com.example.detectiveapplication.api

import com.example.detectiveapplication.models.auth.user_login_response.UserLoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApi {

//    @POST("user/login")
//    suspend fun userLogin(
//        @Field("email") email:String,
//        @Field("password") password:String
//    ):Response<UserLoginResponse>


    @POST("user/login")
    @Headers("Content-Type: application/json")
    suspend fun userLogin(
        @Body body: Map<String, String>
    ):Response<UserLoginResponse>
}