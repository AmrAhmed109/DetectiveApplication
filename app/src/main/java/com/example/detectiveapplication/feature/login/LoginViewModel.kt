package com.example.detectiveapplication.feature.login

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.detectiveapplication.dto.login.UserLoginResponse
import com.example.detectiveapplication.repository.AuthRepository
import com.example.detectiveapplication.repository.DataStoreRepository
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
) : AndroidViewModel(application) {

    //  this variable to check if there is User logged (will use later)
    val readToken = dataStoreRepository.readToken
    var loginResponse: MutableLiveData<NetworkResult<UserLoginResponse>> = MutableLiveData()


    fun saveToken(token: String) = viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveToken(token)
    }

    //     login
    fun login(map: Map<String, String>) {
        viewModelScope.launch {
            getLoginSafeCall(map)
            Log.d("LoginViewModel", " getLoginSafeCall(email,password)")

        }
    }

    private suspend fun getLoginSafeCall(map: Map<String, String>) {
        loginResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {
                val response = authRepository.login(map)
                Log.d("LoginViewModel", "GenericApiResponse: response: ${response.code()}")
                Log.d("LoginViewModel", "GenericApiResponse: response: ${response.body()}")
                Log.d("LoginViewModel", "GenericApiResponse: raw: ${response.raw()}")
                Log.d("LoginViewModel", "GenericApiResponse: headers: ${response.headers()}")
                Log.d("LoginViewModel", "GenericApiResponse: message: ${response.message()}")
                loginResponse.value = handelLoginResponse(response)
            } catch (e: Exception) {
                loginResponse.value = NetworkResult.Error(e.message.toString())
                Log.d("LoginViewModel", "getLoginSafeCall: ${e.message}")
            }
        } else {
            loginResponse.value = NetworkResult.Error("No Internet Connection")
        }

    }


    private fun handelLoginResponse(response: Response<UserLoginResponse>): NetworkResult<UserLoginResponse>? {

        when {
            response.code() == 422 -> {
                Log.d("LoginViewModel", "case 1")
                return NetworkResult.Error("عذا منك توجد مشكلة")
            }
            response.isSuccessful -> {
                if (response.body()?.status == false) {
                    if (response.body()?.error?.email != null) {
                        return NetworkResult.Error(response.body()!!.error.email!!.first())
                    } else if (response.body()?.error?.password != null) {
                        return NetworkResult.Error(response.body()!!.error.password!!.first())
                    } else {
                        return NetworkResult.Error(response.body()!!.message)
                    }
                }
                val userLoginResponse = response.body()
                saveToken("Bearer ${response.body()!!.data.accessToken}")
                return NetworkResult.Success(userLoginResponse!!)
            }
            else -> {
                return NetworkResult.Error(response.body()!!.message)
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