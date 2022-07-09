package com.example.detectiveapplication.feature.notification

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.detectiveapplication.dto.notification.NotificationResponse
import com.example.detectiveapplication.dto.profile_data.UserProfileInfo
import com.example.detectiveapplication.repository.AuthRepository
import com.example.detectiveapplication.repository.DataStoreRepository
import com.example.detectiveapplication.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
    application: Application
) : AndroidViewModel(application) {
    private val tag = "NotificationViewModel"
    var notificationResponse: MutableLiveData<NetworkResult<NotificationResponse>> = MutableLiveData()

    fun getToken():String{
        dataStoreRepository.readToken.let {
            return it
        }
    }

    fun getNotification() {
        viewModelScope.launch {
            dataStoreRepository.readToken.let {
                getNotificationSafeCall(it)
            }
        }
    }

    private suspend fun getNotificationSafeCall(token: String) {
        notificationResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {
                val response = authRepository.notification(token)
                Log.d(tag, "GenericApiResponse: response: ${response.body()}")
                Log.d(tag, "GenericApiResponse: raw: ${response.raw()}")
                Log.d(tag, "GenericApiResponse: headers: ${response.headers()}")
                Log.d(tag, "GenericApiResponse: message: ${response.message()}")
                notificationResponse.value = handleNotificationResponse(response)
            } catch (e: Exception) {
                notificationResponse.value = NetworkResult.Error(e.message.toString())
                Log.d(tag, "getNotificationSafeCall: ${e.message}")
            }
        } else {
            notificationResponse.value = NetworkResult.Error("No Internet Connection")
        }

    }

    private fun handleNotificationResponse(response: Response<NotificationResponse>): NetworkResult<NotificationResponse>? {
        when {
            response.message().contains("Timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 401 -> {
                return NetworkResult.Error(response.message())
            }
            response.code() == 500 -> {
                return NetworkResult.Error(response.message())
            }
            response.code() == 402 -> {
                return NetworkResult.Error(response.message())
            }
            response.isSuccessful -> {
                val notificationResponse = response.body()
                return NetworkResult.Success(notificationResponse!!)
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