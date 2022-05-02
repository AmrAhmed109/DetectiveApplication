package com.example.detectiveapplication.feature.settings

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.detectiveapplication.dto.logout.UserLogoutResponse
import com.example.detectiveapplication.dto.profile_data.UserProfileInfo
import com.example.detectiveapplication.repository.AuthRepository
import com.example.detectiveapplication.repository.DataStoreRepository
import com.example.detectiveapplication.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
    application: Application
) : AndroidViewModel(application) {
    var userInfoResponse: MutableLiveData<NetworkResult<UserProfileInfo>> = MutableLiveData()
    var logoutResponse: MutableLiveData<NetworkResult<UserLogoutResponse>> = MutableLiveData()
    private val tag = "RegistrationViewModel"

    fun saveToken(token: String) = viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveToken(token)
    }
    fun getToken():String{
        dataStoreRepository.readToken.let {
           return it
        }
    }


    // getUserProfileInfo
    fun getUserProfileInfo() {
        viewModelScope.launch {
            dataStoreRepository.readToken.let {
                getUserInfoSafeCall(it)
            }
            Log.d(tag, " getLoginSafeCall(email,password)")

        }
    }

    private suspend fun getUserInfoSafeCall(token: String) {
        userInfoResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {
                val response = authRepository.getUserData(token)
                Log.d(tag, "GenericApiResponse: response: ${response.body()}")
                Log.d(tag, "GenericApiResponse: raw: ${response.raw()}")
                Log.d(tag, "GenericApiResponse: headers: ${response.headers()}")
                Log.d(tag, "GenericApiResponse: message: ${response.message()}")
                userInfoResponse.value = handeluserInfoResponse(response)
            } catch (e: Exception) {
                userInfoResponse.value = NetworkResult.Error(e.message.toString())
                Log.d(tag, "getUserInfoSafeCall: ${e.message}")
            }
        } else {
            userInfoResponse.value = NetworkResult.Error("No Internet Connection")
        }

    }

    private fun handeluserInfoResponse(response: Response<UserProfileInfo>): NetworkResult<UserProfileInfo>? {
        when {
            response.message().toString().contains("Timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 401 -> {
                return NetworkResult.Error(response.body()?.message)
            }
            response.body()!!.name!!.isEmpty() -> {
                Log.d(tag, "handeluserInfoResponse: ${response.body()!!.message}")
                return NetworkResult.Error(response.body()!!.message)
            }
            response.isSuccessful -> {
                val infoResponse = response.body()
                return NetworkResult.Success(infoResponse!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    fun userLogout() {
        viewModelScope.launch {
            dataStoreRepository.readToken.let {
                getUserLogOutSafeCall(it)
            }
            Log.d(tag, " userLogout(email,password)")
        }
    }

    private suspend fun getUserLogOutSafeCall(token: String) {
        logoutResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {
                val response = authRepository.logout(token)
                logoutResponse.value = handelUserLogoutResponse(response)
            } catch (e: Exception) {
                logoutResponse.value = NetworkResult.Error(e.message.toString())
                Log.d(tag, "getUserInfoSafeCall: ${e.message}")
            }
        } else {
            logoutResponse.value = NetworkResult.Error("No Internet Connection")
        }

    }

    private fun handelUserLogoutResponse(response: Response<UserLogoutResponse>): NetworkResult<UserLogoutResponse>? {
        when {
            response
                .message()
                .toString()
                .contains("Timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            
            response.code() == 401 -> {
                return NetworkResult.Error(response.body()?.message)
            }
//            response.body()!!. -> {
//                Log.d(tag, "handeluserInfoResponse: ${response.body()!!.message}")
//                return NetworkResult.Error(response.body()!!.message)
//            }
            response.isSuccessful -> {
                val infoResponse = response.body()
                saveToken("")
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