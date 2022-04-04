package com.example.detectiveapplication.feature.reset_password

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.detectiveapplication.dto.auth_response.forget_password.ForgetPasswordResponse
import com.example.detectiveapplication.dto.auth_response.login.UserLoginResponse
import com.example.detectiveapplication.repository.AuthRepository
import com.example.detectiveapplication.repository.DataStoreRepository
import com.example.detectiveapplication.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
    application: Application
) : AndroidViewModel(application) {
    private val tag = "ResetPasswordViewModel"
    var forgetPasswordResponse: MutableLiveData<NetworkResult<ForgetPasswordResponse>> = MutableLiveData()

    fun saveToken(token :String)= viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveToken(token)
    }

    // forgetPassword
    fun forgetPassword(map:Map<String, String>) {
        viewModelScope.launch {
            getForgetPasswordSafeCall(map)
            Log.d(tag, " getForgetPasswordSafeCall(email)")

        }
    }
    private suspend fun getForgetPasswordSafeCall(map:Map<String, String>) {
        forgetPasswordResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {
                val response = authRepository.forgetPassword(map)
                forgetPasswordResponse.value = handelForgetPasswordResponse(response)
            } catch (e: Exception) {
                forgetPasswordResponse.value = NetworkResult.Error(e.message.toString())
                Log.d(tag, "getLoginSafeCall: ${e.message}")
            }
        } else {
            forgetPasswordResponse.value = NetworkResult.Error("No Internet Connection")
        }

    }

    private fun handelForgetPasswordResponse(response: Response<ForgetPasswordResponse>): NetworkResult<ForgetPasswordResponse>? {
        when {
            response.message().toString().contains("Timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 422 -> {
                return NetworkResult.Error(response.body()?.message)
            }
            response.body()!!.status == false -> {
                Log.d(tag, "handelForgetPasswordResponse: ${response.body()!!.message}")
                return NetworkResult.Error( response.body()?.error?.first() )
            }
            response.isSuccessful -> {
                val userForgetPasswordResponse = response.body()
                return NetworkResult.Success(userForgetPasswordResponse!!)
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