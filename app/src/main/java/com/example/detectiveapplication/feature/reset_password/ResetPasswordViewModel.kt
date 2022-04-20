package com.example.detectiveapplication.feature.reset_password

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.detectiveapplication.dto.reset_password.CodeVerificationResponse
import com.example.detectiveapplication.dto.reset_password.ForgetPasswordResponse
import com.example.detectiveapplication.dto.reset_password.ResetPasswordResponse
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
    var resetPasswordResponse: MutableLiveData<NetworkResult<ResetPasswordResponse>> = MutableLiveData()
    var checkCodeResponse: MutableLiveData<NetworkResult<CodeVerificationResponse>> = MutableLiveData()

    // forgetPassword
    fun forgetPassword(map: Map<String, String>) {
        viewModelScope.launch {
            getForgetPasswordSafeCall(map)
            Log.d(tag, " getForgetPasswordSafeCall(email)")

        }
    }

    private suspend fun getForgetPasswordSafeCall(map: Map<String, String>) {
        forgetPasswordResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {
                val response = authRepository.forgetPassword(map)
                Log.d(tag, "GenericApiResponse: response: ${response.body()}")
                Log.d(tag, "GenericApiResponse: raw: ${response.raw()}")
                Log.d(tag, "GenericApiResponse: headers: ${response.headers()}")
                Log.d(tag, "GenericApiResponse: message: ${response.message()}")
                forgetPasswordResponse.value = handleForgetPasswordResponse(response)
            } catch (e: Exception) {
                forgetPasswordResponse.value = NetworkResult.Error(e.message.toString())
                Log.d(tag, "try catch error: ${e.message}")
            }
        } else {
            forgetPasswordResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private fun handleForgetPasswordResponse(response: Response<ForgetPasswordResponse>): NetworkResult<ForgetPasswordResponse>? {
        when {
            response.message().toString().contains("Timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.isSuccessful -> {
                if (response.body()?.status == false) {
                    if (response.body()?.error != null) {
                        return NetworkResult.Error(response.body()!!.error.first())
                    } else {
                        return NetworkResult.Error(response.body()!!.message)
                    }
                }
                val userForgetPasswordResponse = response.body()
                return NetworkResult.Success(userForgetPasswordResponse!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }


    // resetPassword
    fun resetPassword(map: Map<String, String>) {
        viewModelScope.launch {
            getResetPasswordSafeCall(map)
            Log.d(tag, " getResetPasswordSafeCall")

        }
    }

    private suspend fun getResetPasswordSafeCall(map: Map<String, String>) {
        resetPasswordResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {
                val response = authRepository.resetPassword(map)
                Log.d(tag, "GenericApiResponse: response: ${response.body()}")
                Log.d(tag, "GenericApiResponse: raw: ${response.raw()}")
                Log.d(tag, "GenericApiResponse: headers: ${response.headers()}")
                Log.d(tag, "GenericApiResponse: message: ${response.message()}")
                resetPasswordResponse.value = handleResetPasswordResponse(response)
            } catch (e: Exception) {
                resetPasswordResponse.value = NetworkResult.Error(e.message.toString())
                Log.d(tag, "try catch error: ${e.message}")
            }
        } else {
            resetPasswordResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private fun handleResetPasswordResponse(response: Response<ResetPasswordResponse>): NetworkResult<ResetPasswordResponse>? {
        when {
            response.message().toString().contains("Timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.isSuccessful -> {
                if (response.body()?.status == false) {
                    if (response.body()?.error != null) {
                        return NetworkResult.Error(response.body()!!.error.first())
                    } else {
                        return NetworkResult.Error(response.body()!!.message)
                    }
                }
                val userResetPasswordResponse = response.body()
                return NetworkResult.Success(userResetPasswordResponse!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    // codeCheck
    fun codeCheck(map: Map<String, String>) {
        viewModelScope.launch {
            getCodeCheckSafeCall(map)
            Log.d(tag, " codeCheck")

        }
    }

    private suspend fun getCodeCheckSafeCall(map: Map<String, String>) {
        checkCodeResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {
                val response = authRepository.codeCheck(map)
                Log.d(tag, "GenericApiResponse: response: ${response.body()}")
                Log.d(tag, "GenericApiResponse: raw: ${response.raw()}")
                Log.d(tag, "GenericApiResponse: headers: ${response.headers()}")
                Log.d(tag, "GenericApiResponse: message: ${response.message()}")
                checkCodeResponse.value = handleCodeCheckResponse(response)
            } catch (e: Exception) {
                checkCodeResponse.value = NetworkResult.Error(e.message.toString())
                Log.d(tag, "try catch error: ${e.message}")
            }
        } else {
            checkCodeResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private fun handleCodeCheckResponse(response: Response<CodeVerificationResponse>): NetworkResult<CodeVerificationResponse>? {
        when {
            response.message().toString().contains("Timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.isSuccessful -> {
                if (response.body()?.status == false) {
                        return NetworkResult.Error(response.body()!!.message)
                }
                val userCodeCheckResponse = response.body()
                return NetworkResult.Success(userCodeCheckResponse!!)
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