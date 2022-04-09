package com.example.detectiveapplication.repository

import com.example.detectiveapplication.response.ActiveCasesResponse
import com.example.detectiveapplication.service.api.CaseService
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject


@ViewModelScoped
class CaseRepository @Inject constructor(private val caseService: CaseService) {

    suspend fun getAllActiveCases(token: String): Result<ActiveCasesResponse> = kotlin.runCatching {
        caseService.fetchActiveCases(token)
    }
}