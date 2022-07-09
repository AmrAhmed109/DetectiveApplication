package com.example.detectiveapplication.feature.edit_profile

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.detectiveapplication.dto.edit_profile.EditProfileResponse
import com.example.detectiveapplication.repository.AuthRepository
import com.example.detectiveapplication.repository.DataStoreRepository
import com.example.detectiveapplication.utils.NetworkResult
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
    application: Application
) : AndroidViewModel(application) {
    var profileResponse: MutableLiveData<NetworkResult<EditProfileResponse>> = MutableLiveData()

    fun getToken(): String {
        dataStoreRepository.readToken.let {
            return it
        }
    }

    fun editProfile(name: String, email: String, password: String, confirm: String) =
        viewModelScope.launch {
            val map: HashMap<String, String> = HashMap()
            map["name"] = name
            map["email"] = email
            map["password"] = password
            map["password_confirmation"] = confirm

            getUserEditProfile(map)
        }

    private suspend fun getUserEditProfile(map: Map<String, String>) {
        profileResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {
                val response = repository.editProfile(getToken(), map)
                profileResponse.value = handleEditProfileResponse(response)
            } catch (e: Exception) {
                profileResponse.value = NetworkResult.Error(e.message.toString())
            }
        } else {
            profileResponse.value = NetworkResult.Error("No Internet Connection")
        }

    }

    private fun handleEditProfileResponse(response: Response<EditProfileResponse>): NetworkResult<EditProfileResponse> {
        when {
            response.isSuccessful -> {
                if (response.body()?.status == false) {
                    return NetworkResult.Error(response.body()!!.error.first())
                } else {
                    val profileResponse = response.body()
                    return NetworkResult.Success(profileResponse!!)
                }
            }
            response.message().toString().contains("Timeout") -> {
                return NetworkResult.Error("Timeout")
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