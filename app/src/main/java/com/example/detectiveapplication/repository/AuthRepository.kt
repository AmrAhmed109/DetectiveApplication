package com.example.detectiveapplication.repository

import com.example.detectiveapplication.dto.caseDetails.CaseDetailsResponse
import com.example.detectiveapplication.dto.createKid.CreateFoundKidResponse
import com.example.detectiveapplication.dto.createKid.CreateKidnappedResponse
import com.example.detectiveapplication.dto.edit_profile.EditProfileResponse
import com.example.detectiveapplication.dto.followStatues.FollowStatuesSaveResponse
import com.example.detectiveapplication.dto.followedCases.FollowedCasesItem
import com.example.detectiveapplication.dto.followedCases.FollowedCasesResponse
import com.example.detectiveapplication.dto.reset_password.ForgetPasswordResponse
import com.example.detectiveapplication.dto.login.UserLoginResponse
import com.example.detectiveapplication.dto.logout.UserLogoutResponse
import com.example.detectiveapplication.dto.notification.NotificationReadResponse
import com.example.detectiveapplication.dto.notification.NotificationResponse
import com.example.detectiveapplication.dto.pendingCases.PendingCasesResponse
import com.example.detectiveapplication.dto.profile_data.UserProfileInfo
import com.example.detectiveapplication.dto.recognition.RecognitionResponse
import com.example.detectiveapplication.dto.registration.UserRegistrationResponse
import com.example.detectiveapplication.dto.reset_password.CodeVerificationResponse
import com.example.detectiveapplication.dto.reset_password.ResetPasswordResponse
import com.example.detectiveapplication.dto.search_response.SearchResponse
import com.example.detectiveapplication.service.api.UserService
import com.example.detectiveapplication.utils.Constants.Companion.requestBodyConvert
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject


@ViewModelScoped
class AuthRepository @Inject constructor(private val authApi: UserService) {


    suspend fun recognition(
        token: String,
        image: MultipartBody.Part
    ): Response<RecognitionResponse> {
        return authApi.recognition(token, image)
    }

    suspend fun searchQuery(
        token: String,
        name:String,
        city:String,
        age:String,
        from:String,
        to:String
    ): Response<SearchResponse> {
        return authApi.searchQuery(token, name, city, age, from,to)
    }


    suspend fun notification(token: String): Response<NotificationResponse> {
        return authApi.notification(token)
    }

    suspend fun notificationRead(token: String,id: String): Response<NotificationReadResponse> {
        return authApi.notificationRead(token,id)
    }

    suspend fun getFollowedCases(token: String): Response<List<FollowedCasesItem>> {
        return authApi.getFollowedCases(token)
    }

    suspend fun getCaseDetails(token: String, id: String): Response<CaseDetailsResponse> {
        return authApi.getDetailsCases(token, id)
    }

    suspend fun addKidToFollowList(token: String, id: String): Response<FollowStatuesSaveResponse> {
        val map = HashMap<String, String>()
        map["kid_id"] = id
        return authApi.addKidToFollowList(token, map)
    }

    suspend fun deleteKidFromFollowList(
        token: String,
        id: String
    ): Response<FollowStatuesSaveResponse> {
        val map = HashMap<String, String>()
        map["kid_id"] = id
        return authApi.deleteKidFromFollowList(token, map)
    }

    suspend fun createFoundKidnappedKid(
        token: String,
        name: String,
        image: MultipartBody.Part,
        other_info: String,
        city: String,
        sub_city: String,
        parent_name: String,
        parent_address: String,
        parent_national_id: String,
        parent_phone_number: String,
        parent_other_info: String,
        kidnap_date: String,
    ): Response<CreateFoundKidResponse> {
        return authApi.createFoundKidnappedKid(
            token = token,
            name = name.requestBodyConvert(),
            image = image,
            other_info = other_info.requestBodyConvert(),
            status = "found".requestBodyConvert(),
            city = city.requestBodyConvert(),
            sub_city = sub_city.requestBodyConvert(),
            parent_name = parent_name.requestBodyConvert(),
            parent_address = parent_address.requestBodyConvert(),
            parent_national_id = parent_national_id.requestBodyConvert(),
            parent_phone_number = parent_phone_number.requestBodyConvert(),
            parent_other_info = parent_other_info.requestBodyConvert(),
            kidnap_date = kidnap_date.requestBodyConvert(),
        )
    }

    suspend fun createKidnappedKid(
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
        parent_image: MultipartBody.Part,
        id_number: String
    ): Response<CreateKidnappedResponse> {
        return authApi.createKidnappedKid(
            token = token,
            name = name.requestBodyConvert(),
            image = image,
            other_info = other_info.requestBodyConvert(),
            status = "not_found".requestBodyConvert(),
            city = city.requestBodyConvert(),
            sub_city = sub_city.requestBodyConvert(),
            parent_name = parent_name.requestBodyConvert(),
            parent_address = parent_address.requestBodyConvert(),
            parent_national_id = parent_national_id.requestBodyConvert(),
            parent_phone_number = parent_phone_number.requestBodyConvert(),
            parent_other_info = parent_other_info.requestBodyConvert(),
            birth_image = birth_image,
            kidnap_date = kidnap_date.requestBodyConvert(),
            age = age.requestBodyConvert(),
            parent_image = parent_image,
            id_number = id_number.requestBodyConvert(),
        )
    }
//suspend fun createKidnappedKid(
//    token: String,
//    image: MultipartBody.Part,
//    birth_image: MultipartBody.Part,
//    map: Map<String,String>
//): Response<CreateKidnappedResponse> {
//    return authApi.createKidnappedKid(token,image,birth_image,map)
//}

    ////////////////////////////////

    suspend fun login(map: Map<String, String>): Response<UserLoginResponse> {
        return authApi.userLogin(map)
    }

    suspend fun registration(map: Map<String, String>): Response<UserRegistrationResponse> {
        return authApi.userRegistration(map)
    }

    suspend fun editProfile(
        token: String,
        map: Map<String, String>
    ): Response<EditProfileResponse> {
        return authApi.userEditProfile(token, map)
    }

    suspend fun getPendingCases(token: String): Response<PendingCasesResponse> {
        return authApi.pendingCases(token)
    }

    suspend fun forgetPassword(map: Map<String, String>): Response<ForgetPasswordResponse> {
        return authApi.userForgetPassword(map)
    }

    suspend fun resetPassword(map: Map<String, String>): Response<ResetPasswordResponse> {
        return authApi.userResetPassword(map)
    }

    suspend fun codeCheck(map: Map<String, String>): Response<CodeVerificationResponse> {
        return authApi.userCodeCheck(map)
    }

    suspend fun logout(token: String): Response<UserLogoutResponse> {
        return authApi.userLogout(token)
    }

    suspend fun getUserData(token: String): Response<UserProfileInfo> {
        return authApi.getUserInfo(token)
    }

}