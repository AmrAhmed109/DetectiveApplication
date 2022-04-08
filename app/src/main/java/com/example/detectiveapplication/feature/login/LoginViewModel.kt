package com.example.detectiveapplication.feature.login

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.*
import com.example.detectiveapplication.base.BaseViewModel
import com.example.detectiveapplication.dto.login.UserLoginResponse
import com.example.detectiveapplication.repository.AuthRepository
import com.example.detectiveapplication.repository.DataStoreRepository
import com.example.detectiveapplication.utils.Constants
import com.example.detectiveapplication.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
    application: Application
) : BaseViewModel(application) {

    //  this variable to check if there is User logged (will use later)
    val readToken = dataStoreRepository.readToken
    var loginResponse: MutableLiveData<NetworkResult<UserLoginResponse>> = MutableLiveData()


    fun saveToken(token: String) = viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveToken(token)
    }

    // login
    fun login(map: Map<String, String>) {
        viewModelScope.launch {
            getLoginSafeCall(map)
            Log.d("getLoginSafeCall", " getLoginSafeCall(email,password)")

        }
    }

    private suspend fun getLoginSafeCall(map: Map<String, String>) {
        loginResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            authRepository.login(map)
                .onSuccess {
                    loginResponse.value = handelLoginResponse(it)
                }
                .onFailure {
                    loginResponse.value = NetworkResult.Error(it.message.toString())
                    handleException(it)
                }
        } else {
            loginResponse.value = NetworkResult.Error("No Internet Connection")
        }

    }


    private fun handelLoginResponse(response: Response<UserLoginResponse>): NetworkResult<UserLoginResponse>? {
        when {
            response.message().toString().contains("Timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("Api Key Limited")
            }
            response.body()!!.data.accessToken.isEmpty() -> {
                Log.d(TAG, "handelLoginResponse: ${response.body()!!.message}")
                return NetworkResult.Error(response.body()!!.message)
            }
            response.isSuccessful -> {
                val userLoginResponse = response.body()
                saveToken("Bearer ${response.body()!!.data.accessToken}")
                Constants.TOKEN = "Bearer ${response.body()!!.data.accessToken}"
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