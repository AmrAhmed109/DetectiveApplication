package com.example.detectiveapplication.feature.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.detectiveapplication.base.BaseViewModel
import com.example.detectiveapplication.feature.home.utils.Capital
import com.example.detectiveapplication.repository.CaseRepository
import com.example.detectiveapplication.repository.DataStoreRepository
import com.example.detectiveapplication.request.CasesSearchRequest
import com.example.detectiveapplication.response.ActiveCasesResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val casesRepository: CaseRepository,
    private val dataStoreRepository: DataStoreRepository,
    application: Application,
) : BaseViewModel(application) {

    private var _cases = MutableLiveData<ActiveCasesResponse>()
    val cases: LiveData<ActiveCasesResponse> = _cases

    private var _selectedCapital = MutableLiveData<Capital>(Capital.All)
    val selectedCapital: LiveData<Capital> = _selectedCapital

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

    fun setSelectedCapital(capital: Capital) {
        _selectedCapital.value = capital
        searchCases(capital)
    }

    private fun searchCases(capital: Capital) = viewModelScope.launch {
        updateLoading(true)

        casesRepository
            .searchCases(
                token = dataStoreRepository.readToken,
                CasesSearchRequest(
                    city = if (capital.capitalName == Capital.All.capitalName)
                        null
                    else
                        capital.capitalName
                )
            )
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
