package com.example.detectiveapplication.dto.caseDetails


import com.google.gson.annotations.SerializedName

data class CaseDetailsResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: DetailData,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)

data class DetailData(
    @SerializedName("age")
    val age: Int,
    @SerializedName("auth_followed")
    val authFollowed: Boolean,
    @SerializedName("birth_image")
    val birthImage: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("guardian")
    val guardian: List<Guardian>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("id_number")
    val idNumber: Any,
    @SerializedName("image")
    val image: String,
    @SerializedName("kid_image")
    val kidImage: List<Any>,
    @SerializedName("kidnap_date")
    val kidnapDate: String,
    @SerializedName("kidnap_status")
    val kidnapStatus: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("other_info")
    val otherInfo: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("sub_city")
    val subCity: String,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("users")
    val users: Users
)
data class Users(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("email_verified_at")
    val emailVerifiedAt: Any,
    @SerializedName("fcm_token")
    val fcmToken: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("status")
    val status: String
)
data class Guardian(
    @SerializedName("address")
    val address: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("id_image")
    val idImage: String,
    @SerializedName("kid_id")
    val kidId: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("other_info")
    val otherInfo: String,
    @SerializedName("parent_national_id")
    val parentNationalId: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user_id")
    val userId: Any
)