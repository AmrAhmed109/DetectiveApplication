package com.example.detectiveapplication.feature.cases

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.detectiveapplication.base.BaseViewModel
import com.example.detectiveapplication.repository.CaseRepository
import com.example.detectiveapplication.repository.DataStoreRepository
import com.example.detectiveapplication.response.ActiveCasesResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CasesViewModel @Inject constructor(
    private val casesRepository: CaseRepository,
    private val dataStoreRepository: DataStoreRepository,
    application: Application,
) : BaseViewModel(application) {

    private var _cases = MutableLiveData<ActiveCasesResponse>()
    val cases: LiveData<ActiveCasesResponse> = _cases

    suspend fun getCases() {
        updateLoading(true)

        dataStoreRepository.readToken.let {
            getCases(it)
        }
    }

    private suspend fun getCases(token: String) {
        casesRepository
            .getAllActiveCases(token)
            .onSuccess {
                updateLoading(false)
                _cases.postValue(it)
            }
            .onFailure {
                updateLoading(false)
                handleException(it)
            }

    }

    fun setCases(cases: ActiveCasesResponse) {

    }
}
