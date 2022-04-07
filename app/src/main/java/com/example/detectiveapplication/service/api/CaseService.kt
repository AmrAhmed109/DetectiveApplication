package com.example.detectiveapplication.service.api

import com.example.detectiveapplication.response.ActiveCasesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface CaseService {

    @GET("/user/kids/index")
    @Headers("Content-Type: application/json")
    suspend fun fetchActiveCases(@Header("Authorization") token: String): Response<ActiveCasesResponse>
}