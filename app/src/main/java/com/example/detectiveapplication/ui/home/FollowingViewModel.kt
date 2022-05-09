package com.example.detectiveapplication.ui.home

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.detectiveapplication.dto.followedCases.FollowedCasesItem
import com.example.detectiveapplication.dto.followedCases.FollowedCasesResponse
import com.example.detectiveapplication.dto.profile_data.UserProfileInfo
import com.example.detectiveapplication.repository.AuthRepository
import com.example.detectiveapplication.repository.CaseRepository
import com.example.detectiveapplication.repository.DataStoreRepository
import com.example.detectiveapplication.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class FollowingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
    application: Application,
) : AndroidViewModel(application) {
    private val tag="FollowingViewModel"
    var followedCasesResponse: MutableLiveData<NetworkResult<List<FollowedCasesItem>>> = MutableLiveData()

    fun getToken():String{
        dataStoreRepository.readToken.let {
            return it
        }
    }
    fun getFollowedCasesInfo() {
        viewModelScope.launch {
                getUserInfoSafeCall(getToken())
        }
    }

    private suspend fun getUserInfoSafeCall(token: String) {
        followedCasesResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {
                val response = authRepository.getFollowedCases(token)
                Log.d(tag, "GenericApiResponse: response: ${response.body()}")
                Log.d(tag, "GenericApiResponse: raw: ${response.raw()}")
                Log.d(tag, "GenericApiResponse: headers: ${response.headers()}")
                Log.d(tag, "GenericApiResponse: message: ${response.message()}")
                followedCasesResponse.value = handleFollowedCasesResponse(response)
            } catch (e: Exception) {
                followedCasesResponse.value = NetworkResult.Error(e.message.toString())
                Log.d(tag, "getUserInfoSafeCall: ${e.message}")
            }
        } else {
            followedCasesResponse.value = NetworkResult.Error("No Internet Connection")
        }

    }

    private fun handleFollowedCasesResponse(response: Response<List<FollowedCasesItem>>): NetworkResult<List<FollowedCasesItem>>? {
        when {
            response.message().toString().contains("Timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.isSuccessful -> {
                val followedResponse = response.body()
                return NetworkResult.Success(followedResponse!!)
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