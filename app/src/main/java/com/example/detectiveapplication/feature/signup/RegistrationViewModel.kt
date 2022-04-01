package com.example.detectiveapplication.feature.signup

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.detectiveapplication.dto.auth_response.registration.UserRegistrationResponse
import com.example.detectiveapplication.repository.AuthRepository
import com.example.detectiveapplication.repository.DataStoreRepository
import com.example.detectiveapplication.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
    application: Application
) : AndroidViewModel(application) {
    val tag ="RegistrationViewModel"
    var registrationResponse: MutableLiveData<NetworkResult<UserRegistrationResponse>> = MutableLiveData()

    fun register(map:Map<String, String>) {
        viewModelScope.launch {
            getRegistrationSafeCall(map)
            Log.d(tag, " getLoginSafeCall(email,password)")
        }
    }

    private suspend fun getRegistrationSafeCall(map:Map<String, String>) {
        registrationResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {
                val response = authRepository.registration(map)
                registrationResponse.value = handelRegistrationResponse(response)
            } catch (e: Exception) {
                registrationResponse.value = NetworkResult.Error(e.message.toString())
                Log.d(tag, "getRegistrationSafeCall: ${e.message}")
            }
        } else {
            registrationResponse.value = NetworkResult.Error("No Internet Connection")
        }

    }


    private fun handelRegistrationResponse(response: Response<UserRegistrationResponse>): NetworkResult<UserRegistrationResponse>? {
        when {
            response.message().toString().contains("Timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("Api Key Limited")
            }
            response.body()!!.data.name.isEmpty() -> {
                Log.d(tag, "handelRegistrationResponse: ${response.body()!!.message}")
                return NetworkResult.Error( response.body()!!.message)
            }
            response.isSuccessful -> {
                val userLoginResponse = response.body()
                return NetworkResult.Success(userLoginResponse!!)
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
