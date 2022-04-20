package com.example.detectiveapplication.service.api

import com.example.detectiveapplication.dto.reset_password.ForgetPasswordResponse
import com.example.detectiveapplication.dto.login.UserLoginResponse
import com.example.detectiveapplication.dto.logout.UserLogoutResponse
import com.example.detectiveapplication.dto.profile_data.UserProfileInfo
import com.example.detectiveapplication.dto.registration.UserRegistrationResponse
import com.example.detectiveapplication.dto.reset_password.CodeVerificationResponse
import com.example.detectiveapplication.dto.reset_password.ResetPasswordResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserService {


    /**
     * User Parameter
     * email - password
     * */
    @POST("user/login")
    @Headers("Content-Type: application/json")
    suspend fun userLogin(
        @Body body: Map<String, String>,
    ): Response<UserLoginResponse>


    /**
     * User Parameter
     * email* - password* - password_confirmation* -image(not required) - name*
     * */
    @POST("user/register")
    @Headers("Content-Type: application/json")
    suspend fun userRegistration(
        @Body body: Map<String, String>
    ): Response<UserRegistrationResponse>
//        @Part image:MultipartBody.Part? = null


    /**
     * User Parameter
     * email*
     * */
    @POST("user/forget-password")
    @Headers("Content-Type: application/json")
    suspend fun userForgetPassword(
        @Body body: Map<String, String>
    ): Response<ForgetPasswordResponse>

    /**
     * Reset Parameter
     * token*
     * password*
     * password_confirmation*
     * */
    @POST("user/reset-password")
    @Headers("Content-Type: application/json")
    suspend fun userResetPassword(
        @Body body: Map<String, String>
    ): Response<ResetPasswordResponse>

    /**
     * code verify Parameter
     * code*
     * */
    @POST("user/reset-password")
    @Headers("Content-Type: application/json")
    suspend fun userCodeCheck(
        @Body body: Map<String, String>
    ): Response<CodeVerificationResponse>


    @POST("user/logout")
    @Headers("Content-Type: application/json")
    suspend fun userLogout(
        @Header("Authorization") token: String
    ): Response<UserLogoutResponse>

    @POST("user/me")
    @Headers("Content-Type: application/json")
    suspend fun getUserInfo(
        @Header("Authorization") token: String
    ): Response<UserProfileInfo>
}