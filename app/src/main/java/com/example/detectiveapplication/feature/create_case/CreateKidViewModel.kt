package com.example.detectiveapplication.feature.create_case

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.detectiveapplication.dto.createKid.CreateKidnappedResponse
import com.example.detectiveapplication.dto.profile_data.UserProfileInfo
import com.example.detectiveapplication.repository.AuthRepository
import com.example.detectiveapplication.repository.DataStoreRepository
import com.example.detectiveapplication.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CreateKidViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
    application: Application
) : AndroidViewModel(application) {
    private val tag = "CreateKidViewModel"
    var createKidnappedKidResponse: MutableLiveData<NetworkResult<CreateKidnappedResponse>> =
        MutableLiveData()

    fun getToken(): String {
        dataStoreRepository.readToken.let {
            return it
        }
    }


    // getUserProfileInfo
    fun createKidnappedKid(
        name: String,
        image: MultipartBody.Part,
        other_info: String,
        status: String,
        city: String,
        sub_city: String,
        parent_name: String,
        parent_address: String,
        parent_national_id: String,
        parent_phone_number: String,
        parent_other_info: String,
        birth_image: MultipartBody.Part,
        kidnap_date: String,
        age: String,
        parent_image:MultipartBody.Part,
        kid_national_id:String
    ) {
        viewModelScope.launch {
            Log.d(tag, "createKidnappedKid: $image  $birth_image ")
            createKidnappedKidSafeCall(
                getToken(),
                name,
                image,
                other_info,
                status,
                city,
                sub_city,
                parent_name,
                parent_address,
                parent_national_id,
                parent_phone_number,
                parent_other_info,
                birth_image,
                kidnap_date,
                age,
                parent_image,
                kid_national_id
            )
//            val map :HashMap<String, String> = HashMap()
//            map["name"] = name
//            map["other_info"] = other_info
//            map["status"] = status
//            map["city"] = city
//            map["sub_city"] = sub_city
//            map["parent_name"] = parent_name
//            map["parent_address"] = parent_address
//            map["parent_national_id"] = parent_national_id
//            map["parent_phone_number"] = parent_phone_number
//            map["parent_other_info"] = parent_other_info
//            map["kidnap_date"] = kidnap_date
//            map["age"] = age
//            createKidnappedKidSafeCall(getToken(),image,birth_image, map)
            Log.d(tag, " createKidnappedKidSafeCall")

        }
    }

    private suspend fun createKidnappedKidSafeCall(
        token: String,
        name: String,
        image: MultipartBody.Part,
        other_info: String,
        status: String,
        city: String,
        sub_city: String,
        parent_name: String,
        parent_address: String,
        parent_national_id: String,
        parent_phone_number: String,
        parent_other_info: String,
        birth_image: MultipartBody.Part,
        kidnap_date: String,
        age: String,
        parent_image:MultipartBody.Part,
        kid_national_id:String
//        map: Map<String,String>
    ) {
        createKidnappedKidResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {
                val response = authRepository.createKidnappedKid(token, name, image, other_info, status, city, sub_city, parent_name, parent_address, parent_national_id, parent_phone_number, parent_other_info, birth_image, kidnap_date, age, parent_image, kid_national_id)
                Log.d(tag, "GenericApiResponse: response: ${response.body()}")
                Log.d(tag, "GenericApiResponse: raw: ${response.raw()}")
                Log.d(tag, "GenericApiResponse: headers: ${response.headers()}")
                Log.d(tag, "GenericApiResponse: message: ${response.message()}")
                createKidnappedKidResponse.value = handleCreateKidnappedKidResponse(response)
            } catch (e: Exception) {
                createKidnappedKidResponse.value = NetworkResult.Error(e.message.toString())
                Log.d(tag, "getUserInfoSafeCall: ${e.message}")
            }
        } else {
            createKidnappedKidResponse.value = NetworkResult.Error("No Internet Connection")
        }

    }

    private fun handleCreateKidnappedKidResponse(response: Response<CreateKidnappedResponse>): NetworkResult<CreateKidnappedResponse>? {
        when {
            response.message().toString().contains("Timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 422 -> {
                return NetworkResult.Error(response.body()?.message.toString())
            }
            response.isSuccessful -> {
                if (response.body()?.code == 404) {
                    return NetworkResult.Error(response.body()!!.message)
                } else if (response.body()?.code == 422) {

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
                    } else if (response.body()?.error?.birth_image!!.isNotEmpty()) {
                        return NetworkResult.Error(response.body()!!.error.birth_image.first())
                    }else if (response.body()?.message!!.contains("error-face-not-found-or-many-faces")) {
                        return NetworkResult.Error(response.body()!!.message)
                    }
                    return NetworkResult.Error(response.body()!!.message)
                }
                val createKidnappedResponse = response.body()
                return NetworkResult.Success(createKidnappedResponse!!)
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