package com.example.detectiveapplication.service.api

import com.example.detectiveapplication.dto.caseDetails.CaseDetailsResponse
import com.example.detectiveapplication.dto.createKid.CreateKidnappedResponse
import com.example.detectiveapplication.dto.edit_profile.EditProfileResponse
import com.example.detectiveapplication.dto.followStatues.FollowStatuesSaveResponse
import com.example.detectiveapplication.dto.followedCases.FollowedCasesItem
import com.example.detectiveapplication.dto.followedCases.FollowedCasesResponse
import com.example.detectiveapplication.dto.reset_password.ForgetPasswordResponse
import com.example.detectiveapplication.dto.login.UserLoginResponse
import com.example.detectiveapplication.dto.logout.UserLogoutResponse
import com.example.detectiveapplication.dto.pendingCases.PendingCasesResponse
import com.example.detectiveapplication.dto.profile_data.UserProfileInfo
import com.example.detectiveapplication.dto.registration.UserRegistrationResponse
import com.example.detectiveapplication.dto.reset_password.CodeVerificationResponse
import com.example.detectiveapplication.dto.reset_password.ResetPasswordResponse
import com.example.detectiveapplication.utils.Constants.Companion.requestBodyConvert
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface UserService {


    /**
     * User Parameter
     * email - password
     * */
    @POST("user/login")
    @Headers("Content-Type: application/json")
    suspend fun userLogin(
        @Body body: Map<String, String>,
    ): Response<UserLoginResponse>


    /**
     * User Parameter
     * email* - password* - password_confirmation* -image(not required) - name*
     * */
    @POST("user/register")
    @Headers("Content-Type: application/json")
    suspend fun userRegistration(
        @Body body: Map<String, String>
    ): Response<UserRegistrationResponse>
//        @Part image:MultipartBody.Part? = null ,"Accept:application/json"

//    @FormUrlEncoded  multipart/form-data
    @Multipart
//    @Headers("Content-Type: application/json")
//    @Headers("content-type:multipart/form-data")
    @POST("user/kiddnaped/create-kid")
    suspend fun createKidnappedKid(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody = "احمد علي".requestBodyConvert(),
        @Part image: MultipartBody.Part,
        @Part("other_info") other_info: RequestBody = "بيانات اخرى".requestBodyConvert(),
        @Part("status") status: RequestBody = "not_found".requestBodyConvert(),
        @Part("city") city: RequestBody = "القاهرة".requestBodyConvert(),
        @Part("sub_city") sub_city: RequestBody = "عين شمس".requestBodyConvert(),
        @Part("parent_name") parent_name: RequestBody = "علي محمد".requestBodyConvert(),
        @Part("parent_address") parent_address: RequestBody = "على ناصية الشارع".requestBodyConvert(),
        @Part("parent_national_id") parent_national_id: RequestBody = "30007258994".requestBodyConvert(),
        @Part("parent_phone_number") parent_phone_number: RequestBody = "01018685229".requestBodyConvert(),
        @Part("parent_other_info") parent_other_info: RequestBody = "بيانات اضافية للوالد".requestBodyConvert(),
        @Part birth_image: MultipartBody.Part,
        @Part("kidnap_date") kidnap_date: RequestBody = "28-8-2022".requestBodyConvert(),
        @Part("age") age: RequestBody = "8".requestBodyConvert(),
        @Part("id_number") id_number: RequestBody = "6546516516".requestBodyConvert(),
    ): Response<CreateKidnappedResponse>

//    @Multipart
//    @POST("user/kiddnaped/create-kid")
//    @Headers("Content-Type: application/json")
//    suspend fun createKidnappedKid(
//        @Header("Authorization") token: String,
//        @Part  image: MultipartBody.Part,
//        @Part birth_image: MultipartBody.Part,
//        @PartMap body: Map<String, String>
//    ): Response<CreateKidnappedResponse>

    /**
     * name*
     * email*
     * password*
     * password_confirmation*
     * */
    @POST("user/auth/update")
    @Headers("Content-Type: application/json")
    suspend fun userEditProfile(
        @Header("Authorization") token: String,
        @Body body: Map<String, String>
    ): Response<EditProfileResponse>

    @GET("user/follow")
    @Headers("Content-Type: application/json")
    suspend fun getFollowedCases(
        @Header("Authorization") token: String
    ): Response<List<FollowedCasesItem>>

    @GET("user/kids/show/{id}")
    @Headers("Content-Type: application/json")
    suspend fun getDetailsCases(
        @Header("Authorization") token: String,
        @Path("id") id:String
    ): Response<CaseDetailsResponse>

    @POST("user/add-follow")
    @Headers("Content-Type: application/json")
    suspend fun addKidToFollowList(
        @Header("Authorization") token: String,
        @Body kid_id :Map<String,String>
    ): Response<FollowStatuesSaveResponse>


    @POST("user/delete-follow")
    @Headers("Content-Type: application/json")
    suspend fun deleteKidFromFollowList(
        @Header("Authorization") token: String,
        @Body kid_id :Map<String,String>
    ): Response<FollowStatuesSaveResponse>

    @GET("user/auth/pending?page=1&limit=700")
    @Headers("Content-Type: application/json")
    suspend fun pendingCases(
        @Header("Authorization") token: String,
    ): Response<PendingCasesResponse>

    /**
     * User Parameter
     * email*
     * */
    @POST("user/forget-password")
    @Headers("Content-Type: application/json")
    suspend fun userForgetPassword(
        @Body body: Map<String, String>
    ): Response<ForgetPasswordResponse>

    /**
     * Reset Parameter
     * token*
     * password*
     * password_confirmation*
     * */
    @POST("user/reset-password")
    @Headers("Content-Type: application/json")
    suspend fun userResetPassword(
        @Body body: Map<String, String>
    ): Response<ResetPasswordResponse>

    /**
     * code verify Parameter
     * code*
     * */
    @POST("user/reset-password")
    @Headers("Content-Type: application/json")
    suspend fun userCodeCheck(
        @Body body: Map<String, String>
    ): Response<CodeVerificationResponse>

    @POST("user/logout")
    @Headers("Content-Type: application/json")
    suspend fun userLogout(
        @Header("Authorization") token: String
    ): Response<UserLogoutResponse>

    @POST("user/me")
    @Headers("Content-Type: application/json")
    suspend fun getUserInfo(
        @Header("Authorization") token: String
    ): Response<UserProfileInfo>
}