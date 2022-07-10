package com.example.detectiveapplication.feature.create_case

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.detectiveapplication.dto.createKid.CreateFoundKidResponse
import com.example.detectiveapplication.repository.AuthRepository
import com.example.detectiveapplication.repository.DataStoreRepository
import com.example.detectiveapplication.utils.NetworkResult
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class CreateStrangerCaseViewModel @Inject constructor(
    private val apiRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
    application: Application
) : AndroidViewModel(application) {
    private val tag = "CreateStrangerCaseViewModel"
    var createFoundKidResponse : MutableLiveData<NetworkResult<CreateFoundKidResponse>> = MutableLiveData()

    private fun token():String{
        return dataStoreRepository.readToken.let {
            it
        }
    }

    fun createFoundKid(name: String, image: MultipartBody.Part, other_info: String, city: String, sub_city: String, parent_name: String, parent_address: String, parent_national_id: String, parent_phone_number: String, parent_other_info: String, kidnap_date: String){
        viewModelScope.launch {
            handleCreateFoundKidSafeCall(token(),name, image, other_info, city, sub_city, parent_name, parent_address, parent_national_id, parent_phone_number, parent_other_info, kidnap_date)
        }
    }

    private suspend fun handleCreateFoundKidSafeCall(token: String, name: String, image: MultipartBody.Part, other_info: String, city: String, sub_city: String, parent_name: String, parent_address: String, parent_national_id: String, parent_phone_number: String, parent_other_info: String, kidnap_date: String) {
        createFoundKidResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()){
            try {
                val response = apiRepository.createFoundKidnappedKid(token, name, image, other_info, city, sub_city, parent_name, parent_address, parent_national_id, parent_phone_number, parent_other_info, kidnap_date)
                Log.d(tag, "GenericApiResponse: response: ${response.body()}")
                Log.d(tag, "GenericApiResponse: raw: ${response.raw()}")
                Log.d(tag, "GenericApiResponse: headers: ${response.headers()}")
                Log.d(tag, "GenericApiResponse: message: ${response.message()}")
                createFoundKidResponse.value = handleCreateFoundKidResponse(response)

            }catch (e: Exception){
                createFoundKidResponse.value = NetworkResult.Error(e.message.toString())
            }
        }

    }

    private fun handleCreateFoundKidResponse(response: Response<CreateFoundKidResponse>): NetworkResult<CreateFoundKidResponse>? {

        when{
            response.message().contains("Timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 422 -> {
                return NetworkResult.Error(response.body()?.message.toString())
            }
            response.isSuccessful->{
                if (response.body()!!.code == 404){
                    return NetworkResult.Error(response.body()?.message.toString())
                }else if (response.body()!!.code == 422){
                    if (response.body()?.error?.name!!.isNotEmpty()) {
                        return NetworkResult.Error(response.body()!!.error.name.first())
                    } else if (response.body()?.error?.image!!.isNotEmpty()) {
                        return NetworkResult.Error(response.body()!!.error.image.first())
                    } else if (response.body()?.error?.otherInfo!!.isNotEmpty()) {
                        return NetworkResult.Error(response.body()!!.error.otherInfo.first())
                    } else if (response.body()?.error?.status!!.isNotEmpty()) {
                        return NetworkResult.Error(response.body()!!.error.status.first())
                    } else if (response.body()?.error?.city!!.isNotEmpty()) {
                        return NetworkResult.Error(response.body()!!.error.city.first())
                    } else if (response.body()?.error?.parentName!!.isNotEmpty()) {
                        return NetworkResult.Error(response.body()!!.error.parentName.first())
                    } else if (response.body()?.error?.parentAddress!!.isNotEmpty()) {
                        return NetworkResult.Error(response.body()!!.error.parentAddress.first())
                    } else if (response.body()?.error?.parentNationalId!!.isNotEmpty()) {
                        return NetworkResult.Error(response.body()!!.error.parentNationalId.first())
                    } else if (response.body()?.error?.parentPhoneNumber!!.isNotEmpty()) {
                        return NetworkResult.Error(response.body()!!.error.parentNationalId.first())
                    } else if (response.body()?.error?.parentOtherInfo!!.isNotEmpty()) {
                        return NetworkResult.Error(response.body()!!.error.parentOtherInfo.first())
                    }
                    return NetworkResult.Error(response.body()!!.message)
                }else if (response.body()?.message!!.contains("error-face-not-found-or-many-faces")) {
                    return NetworkResult.Error(response.body()!!.message)
                }else if (response.body()?.message!!.contains("error-face-not-found-or-many-faces")) {
                    return NetworkResult.Error(response.body()!!.message)
                }
                val foundKidResponse = response.body()
                return NetworkResult.Success(foundKidResponse!!)
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
