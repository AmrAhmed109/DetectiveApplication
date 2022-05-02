package com.example.detectiveapplication.dto.edit_profile


import com.google.gson.annotations.SerializedName

data class EditProfileResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: ResponseData,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("error")
    val error: List<String>,

)

data class ResponseData(
    @SerializedName("created_at")
    val createdAt: Any,
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