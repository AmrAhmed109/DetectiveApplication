package com.example.detectiveapplication.service.api

import com.example.detectiveapplication.request.CasesSearchRequest
import com.example.detectiveapplication.response.ActiveCasesResponse
import com.example.detectiveapplication.service.api.utils.Parameter
import com.example.detectiveapplication.utils.Routing
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject


interface CaseService {

    suspend fun fetchAllActiveCases(token: String): ActiveCasesResponse
    suspend fun fetchUserActiveCases(token: String): ActiveCasesResponse
    suspend fun searchCases(
        token: String,
        casesSearchRequest: CasesSearchRequest
    ): ActiveCasesResponse
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

    override suspend fun searchCases(
        token: String,
        casesSearchRequest: CasesSearchRequest
    ): ActiveCasesResponse {
        return client.get(Routing.FETCH_ALL_CASES) {
            headers.append(
                name = HttpHeaders.Authorization,
                value = token
            )

            if (casesSearchRequest.city != null) {
                parameter(
                    key = Parameter.CITY,
                    value = casesSearchRequest.city
                )
            }

            if (casesSearchRequest.age != null) {
                parameter(
                    key = Parameter.AGE,
                    value = casesSearchRequest.age
                )
            }
        }
    }
}
