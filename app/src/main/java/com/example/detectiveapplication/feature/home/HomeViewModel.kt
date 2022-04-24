package com.example.detectiveapplication.feature.home

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
class HomeViewModel @Inject constructor(
    private val casesRepository: CaseRepository,
    private val dataStoreRepository: DataStoreRepository,
    application: Application,
) : BaseViewModel(application) {

    private var _cases = MutableLiveData<ActiveCasesResponse>()
    val cases: LiveData<ActiveCasesResponse> = _cases

    suspend fun getFeedCases() {
        updateLoading(true)

        dataStoreRepository.readToken.let {
            getFeedCases(it)
        }
    }

    private suspend fun getFeedCases(token: String) {
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
}
