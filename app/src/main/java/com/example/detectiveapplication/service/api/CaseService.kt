package com.example.detectiveapplication.service.api

import com.example.detectiveapplication.dto.cases.Case
import com.example.detectiveapplication.dto.forget_password.ForgetPasswordResponse
import com.example.detectiveapplication.dto.login.UserLoginResponse
import com.example.detectiveapplication.dto.logout.UserLogoutResponse
import com.example.detectiveapplication.dto.profile_data.UserProfileInfo
import com.example.detectiveapplication.dto.registration.UserRegistrationResponse
import com.example.detectiveapplication.response.ActiveCasesResponse
import retrofit2.Response
import retrofit2.http.*

interface CaseService {

    /**
     * User Parameter
     * email - password
     * */
    @GET("api/user/kids/index")
    @Headers("Content-Type: application/json")
    suspend fun fetchActiveCases(): Response<ActiveCasesResponse>

}