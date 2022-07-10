package com.example.detectiveapplication.feature.cases

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.detectiveapplication.base.BaseViewModel
import com.example.detectiveapplication.dto.cases.CloseCaseResponse
import com.example.detectiveapplication.dto.notification.NotificationResponse
import com.example.detectiveapplication.repository.AuthRepository
import com.example.detectiveapplication.repository.CaseRepository
import com.example.detectiveapplication.repository.DataStoreRepository
import com.example.detectiveapplication.response.ActiveCasesResponse
import com.example.detectiveapplication.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CasesViewModel @Inject constructor(
    private val casesRepository: CaseRepository,
    private val authRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
    application: Application,
) : BaseViewModel(application) {

    private val tag = "CasesViewModel"
    var closedCaseResponse: MutableLiveData<NetworkResult<CloseCaseResponse>> = MutableLiveData()

    private var _cases = MutableLiveData<ActiveCasesResponse>()
    val cases: LiveData<ActiveCasesResponse> = _cases

    fun getToken(): String {
        dataStoreRepository.readToken.let {
            return it
        }
    }

    suspend fun getCases() {
        updateLoading(true)

        dataStoreRepository.readToken.let {
            getCases(it)
        }
    }

    private suspend fun getCases(token: String) {
        casesRepository
            .getUserCases(token)
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

    fun closeCase(caseId: String) {
        viewModelScope.launch {
            dataStoreRepository.readToken.let {
                closeCaseSafeCall(it, caseId)
            }
        }
    }

    private suspend fun closeCaseSafeCall(token: String, caseId: String) {
        closedCaseResponse.value = NetworkResult.Loading()
        updateLoading(true)

        if (hasInternetConnection()) {
            try {
                val response = authRepository.closeCase(token, caseId)
                Log.d(tag, "GenericApiResponse: response: ${response.body()}")
                Log.d(tag, "GenericApiResponse: raw: ${response.raw()}")
                Log.d(tag, "GenericApiResponse: headers: ${response.headers()}")
                Log.d(tag, "GenericApiResponse: message: ${response.message()}")
                closedCaseResponse.value = handelCloseCaseResponse(response)
            } catch (e: Exception) {
                updateLoading(false)
                closedCaseResponse.value = NetworkResult.Error(e.message.toString())
                Log.d(tag, "closeCaseSafeCall: ${e.message}")
            }
        } else {
            updateLoading(false)
            closedCaseResponse.value = NetworkResult.Error("No Internet Connection")
        }

    }

    private fun handelCloseCaseResponse(response: Response<CloseCaseResponse>): NetworkResult<CloseCaseResponse>? {
        when {
            response.message().contains("Timeout") -> {
                updateLoading(false)
                return NetworkResult.Error("Timeout")
            }
            response.code() == 401 -> {
                updateLoading(false)
                return NetworkResult.Error(response.message())
            }
            response.code() == 500 -> {
                updateLoading(false)
                return NetworkResult.Error(response.message())
            }
            response.code() == 402 -> {
                updateLoading(false)
                return NetworkResult.Error(response.body()?.message)
            }
            response.isSuccessful -> {
                updateLoading(false)
                val closeResponse = response.body()
                return NetworkResult.Success(closeResponse!!)
            }
            else -> {
                updateLoading(false)
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}
