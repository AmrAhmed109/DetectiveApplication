package com.example.detectiveapplication.service.api

import com.example.detectiveapplication.response.ActiveCasesResponse
import com.example.detectiveapplication.utils.Routing
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject


interface CaseService {

    suspend fun fetchAllActiveCases(token: String): ActiveCasesResponse
    suspend fun fetchUserActiveCases(token: String): ActiveCasesResponse
}

class CaseServiceImpl @Inject constructor(
    private val client: HttpClient
) : CaseService {

    override suspend fun fetchAllActiveCases(token: String): ActiveCasesResponse =
        client.get(Routing.FETCH_ALL_CASES) {
            headers.append(
                name = HttpHeaders.Authorization,
                value = token
            )
        }

    override suspend fun fetchUserActiveCases(token: String): ActiveCasesResponse =
        client.get(Routing.FETCH_USER_CASES) {
            headers.append(
                name = HttpHeaders.Authorization,
                value = token
            )
        }
}
