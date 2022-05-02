package com.example.detectiveapplication.ui.home

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.detectiveapplication.dto.pendingCases.PendingCasesResponse
import com.example.detectiveapplication.dto.profile_data.UserProfileInfo
import com.example.detectiveapplication.repository.AuthRepository
import com.example.detectiveapplication.repository.DataStoreRepository
import com.example.detectiveapplication.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class WatingCasesViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
    application: Application
) : AndroidViewModel(application) {
    var pendingCasesResponse: MutableLiveData<NetworkResult<PendingCasesResponse>> = MutableLiveData()
    private val tag = "WatingCasesViewModel"

    fun getToken():String{
        dataStoreRepository.readToken.let {
            return it
        }
    }

    fun getUserPendingCases() {
        viewModelScope.launch {
                getUserUserPendingCasesSafeCall(getToken())
        }
    }

    private suspend fun getUserUserPendingCasesSafeCall(token: String) {
        pendingCasesResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {
                val response = authRepository.getPendingCases(token)
                Log.d(tag, "GenericApiResponse: response: ${response.body()}")
                Log.d(tag, "GenericApiResponse: raw: ${response.raw()}")
                Log.d(tag, "GenericApiResponse: headers: ${response.headers()}")
                Log.d(tag, "GenericApiResponse: message: ${response.message()}")
                pendingCasesResponse.value = handleUserUserPendingCasesResponse(response)
            } catch (e: Exception) {
                pendingCasesResponse.value = NetworkResult.Error(e.message.toString())
            }
        } else {
            pendingCasesResponse.value = NetworkResult.Error("No Internet Connection")
        }

    }

    private fun handleUserUserPendingCasesResponse(response: Response<PendingCasesResponse>): NetworkResult<PendingCasesResponse>? {
        when {
            response.message().contains("Timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 422 -> {
                return NetworkResult.Error(response.body()?.code.toString())
            }
            response.isSuccessful -> {
                val infoResponse = response.body()
                Log.d(tag, "GenericApiResponse: isSuccessful")

                return NetworkResult.Success(infoResponse!!)
            }
            else -> {
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