package com.example.detectiveapplication.feature.home.faceDetection

import android.app.Application
import android.content.Context
import android.media.Image
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.detectiveapplication.dto.recognition.RecognitionResponse
import com.example.detectiveapplication.repository.AuthRepository
import com.example.detectiveapplication.repository.DataStoreRepository
import com.example.detectiveapplication.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class FaceDetectionModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
    application: Application
) : AndroidViewModel(application) {
    val tag = "FaceDetectionModel"
    var recognitionResponse: MutableLiveData<NetworkResult<RecognitionResponse>> = MutableLiveData()

    private fun token(): String {
        return dataStoreRepository.readToken
    }

    fun recognize(image: MultipartBody.Part) = viewModelScope.launch {
        recognizeSafeCall(token(), image)
    }

    private suspend fun recognizeSafeCall(token: String, image: MultipartBody.Part) {
        recognitionResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {
                val response = authRepository.recognition(token, image)
                Log.d(tag, "GenericApiResponse: response: ${response.body()}")
                Log.d(tag, "GenericApiResponse: raw: ${response.raw()}")
                Log.d(tag, "GenericApiResponse: headers: ${response.headers()}")
                Log.d(tag, "GenericApiResponse: message: ${response.message()}")
                recognitionResponse.value = handleRecognitionResponse(response)
            } catch (e: Exception) {
                recognitionResponse.value = NetworkResult.Error(e.message)
                Log.d(tag, "Error: ${e.message}")

            }
        }
    }
    private fun handleRecognitionResponse(response: Response<RecognitionResponse>): NetworkResult<RecognitionResponse>? {
        when {
            response.message().toString().contains("Timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 422 -> {
                return NetworkResult.Error(response.body()?.message.toString())
            }

            response.code() == 422 -> {
                return NetworkResult.Error("Machine Learning Model Isn't live")
            }

            response.isSuccessful -> {
                if (response.body()?.code == 404) {
                    return NetworkResult.Error(response.body()!!.message)

                } else if (response.body()?.code == 422) {

                    if (response.body()!!.data.isNullOrEmpty()) {
                        return NetworkResult.Error(response.body()!!.message)
                    }

                    return NetworkResult.Error(response.body()!!.message)
                }
                if (response.body()!!.data.isNullOrEmpty()) {
                    return NetworkResult.Error(response.body()!!.message)
                }else if (response.body()?.message!!.contains("error-face-not-found-or-many-faces")) {
                    return NetworkResult.Error("يرجى التأكد من الصورة الختارة للشخص")
                }else if (response.body()?.message!!.contains("we can not found any kid in database")) {
                    return NetworkResult.Error("لا يوجد هذا الشخص في قاعدة بياناتنا, يمكنك اضافة في قاعدة للبيانات")
                }
                val recognitionResponse = response.body()
                return NetworkResult.Success(recognitionResponse!!)
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