package com.example.detectiveapplication.feature.search

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.detectiveapplication.dto.edit_profile.EditProfileResponse
import com.example.detectiveapplication.dto.search_response.SearchResponse
import com.example.detectiveapplication.repository.AuthRepository
import com.example.detectiveapplication.repository.DataStoreRepository
import com.example.detectiveapplication.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
    application: Application
) : AndroidViewModel(application) {

    private val tag = "SearchViewModel"
    var searchResponse: MutableLiveData<NetworkResult<SearchResponse>> = MutableLiveData()

    fun getToken(): String {
        dataStoreRepository.readToken.let {
            return it
        }
    }

    fun query(query: String) =
        viewModelScope.launch {
            getQuery(query)
        }

    private suspend fun getQuery(query: String) {
        searchResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {
                val response = repository.searchQuery(getToken(), query)
                Log.d(tag, "GenericApiResponse: response: ${response.body()}")
                Log.d(tag, "GenericApiResponse: raw: ${response.raw()}")
                Log.d(tag, "GenericApiResponse: headers: ${response.headers()}")
                Log.d(tag, "GenericApiResponse: message: ${response.message()}")
                searchResponse.value = handleQueryResponse(response)
            } catch (e: Exception) {
                searchResponse.value = NetworkResult.Error(e.message.toString())
            }
        } else {
            searchResponse.value = NetworkResult.Error("No Internet Connection")
        }

    }

    private fun handleQueryResponse(response: Response<SearchResponse>): NetworkResult<SearchResponse> {
        when {
            response.message().contains("Timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 422 -> {
                return NetworkResult.Error(response.body()?.code.toString())
            }
            response.isSuccessful -> {
                val queryResponse = response.body()
                Log.d(tag, "GenericApiResponse: isSuccessful")

                return NetworkResult.Success(queryResponse!!)
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