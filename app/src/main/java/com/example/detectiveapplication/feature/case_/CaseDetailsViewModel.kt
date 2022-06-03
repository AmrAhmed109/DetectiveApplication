package com.example.detectiveapplication.feature.case_

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.detectiveapplication.dto.caseDetails.CaseDetailsResponse
import com.example.detectiveapplication.dto.followStatues.FollowStatuesSaveResponse
import com.example.detectiveapplication.dto.followedCases.FollowedCasesItem
import com.example.detectiveapplication.repository.AuthRepository
import com.example.detectiveapplication.repository.DataStoreRepository
import com.example.detectiveapplication.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class CaseDetailsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
    application: Application,
) : AndroidViewModel(application) {
    private val tag="CaseDetailsViewModel"
    val caseDetailResponse :MutableLiveData<NetworkResult<CaseDetailsResponse>> =  MutableLiveData()
    val addKidResponse :MutableLiveData<NetworkResult<FollowStatuesSaveResponse>> =  MutableLiveData()
    val removeKidResponse :MutableLiveData<NetworkResult<FollowStatuesSaveResponse>> =  MutableLiveData()

    fun getToken():String{
        dataStoreRepository.readToken.let {
            return it
        }
    }

    fun getDetailCasesInfo(id:String) {
        viewModelScope.launch {
            getDetailCasesSafeCall(getToken(),id)
        }
    }

    private suspend fun getDetailCasesSafeCall(token: String,id: String) {
        caseDetailResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {
                val response = authRepository.getCaseDetails(token, id)
                Log.d(tag, "GenericApiResponse: response: ${response.body()}")
                Log.d(tag, "GenericApiResponse: raw: ${response.raw()}")
                Log.d(tag, "GenericApiResponse: headers: ${response.headers()}")
                Log.d(tag, "GenericApiResponse: message: ${response.message()}")
                caseDetailResponse.value = handleDetailCasesResponse(response)
            } catch (e: Exception) {
                caseDetailResponse.value = NetworkResult.Error(e.message.toString())
                Log.d(tag, "getUserInfoSafeCall: ${e.message}")
            }
        } else {
            caseDetailResponse.value = NetworkResult.Error("No Internet Connection")
        }

    }

    private fun handleDetailCasesResponse(response: Response<CaseDetailsResponse>): NetworkResult<CaseDetailsResponse>? {
        when {
            response.message().toString().contains("Timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.isSuccessful -> {
                val detailResponse = response.body()
                Log.d(tag, "handleDetailCasesResponse: detailResponse")
                return NetworkResult.Success(detailResponse!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    fun addKidInfo(id:String) {
        viewModelScope.launch {
            getAddKidSafeCall(getToken(),id)
        }
    }

    private suspend fun getAddKidSafeCall(token: String,id: String) {
        addKidResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {
                val response = authRepository.addKidToFollowList(token, id)
                Log.d(tag, "GenericApiResponse: response: ${response.body()}")
                Log.d(tag, "GenericApiResponse: raw: ${response.raw()}")
                Log.d(tag, "GenericApiResponse: headers: ${response.headers()}")
                Log.d(tag, "GenericApiResponse: message: ${response.message()}")
                addKidResponse.value = handleAddKidResponse(response)
            } catch (e: Exception) {
                caseDetailResponse.value = NetworkResult.Error(e.message.toString())
                Log.d(tag, "getUserInfoSafeCall: ${e.message}")
            }
        } else {
            addKidResponse.value = NetworkResult.Error("No Internet Connection")
        }

    }

    private fun handleAddKidResponse(response: Response<FollowStatuesSaveResponse>): NetworkResult<FollowStatuesSaveResponse>? {
        when {
            response.message().toString().contains("Timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.isSuccessful -> {
                val addResponse = response.body()
                return NetworkResult.Success(addResponse!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    fun removeKidInfo(id:String) {
        viewModelScope.launch {
            getRemoveKidSafeCall(getToken(),id)
        }
    }

    private suspend fun getRemoveKidSafeCall(token: String,id: String) {
        removeKidResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {
                val response = authRepository.deleteKidFromFollowList(token, id)
                Log.d(tag, "GenericApiResponse: response: ${response.body()}")
                Log.d(tag, "GenericApiResponse: raw: ${response.raw()}")
                Log.d(tag, "GenericApiResponse: headers: ${response.headers()}")
                Log.d(tag, "GenericApiResponse: message: ${response.message()}")
                removeKidResponse.value = handleRemoveKidResponse(response)
            } catch (e: Exception) {
                caseDetailResponse.value = NetworkResult.Error(e.message.toString())
                Log.d(tag, "getUserInfoSafeCall: ${e.message}")
            }
        } else {
            removeKidResponse.value = NetworkResult.Error("No Internet Connection")
        }

    }

    private fun handleRemoveKidResponse(response: Response<FollowStatuesSaveResponse>): NetworkResult<FollowStatuesSaveResponse>? {
        when {
            response.message().toString().contains("Timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.isSuccessful -> {
                val addResponse = response.body()
                return NetworkResult.Success(addResponse!!)
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