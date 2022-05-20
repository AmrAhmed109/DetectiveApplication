package com.example.detectiveapplication.repository

import com.example.detectiveapplication.dto.cases.Case
import com.example.detectiveapplication.request.CasesSearchRequest
import com.example.detectiveapplication.response.ActiveCasesResponse
import com.example.detectiveapplication.service.api.CaseService
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject


@ViewModelScoped
class CaseRepository @Inject constructor(private val caseService: CaseService) {

    suspend fun getAllActiveCases(token: String): Result<ActiveCasesResponse> = kotlin.runCatching {
        caseService.fetchAllActiveCases(token)
    }

    suspend fun getUserCases(token: String): Result<ActiveCasesResponse> = kotlin.runCatching {
        caseService.fetchUserActiveCases(token)
    }

    suspend fun searchCases(token: String, casesSearchRequest: CasesSearchRequest): Result<ActiveCasesResponse> {
        return kotlin.runCatching {
            caseService.searchCases(token, casesSearchRequest)
        }
    }
}