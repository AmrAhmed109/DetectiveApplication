package com.example.detectiveapplication.dto.auth_response.profile_data


import com.google.gson.annotations.SerializedName

data class UserProfileInfo(
    @SerializedName("code")
    val code: Int,
    @SerializedName("created_at")
    val createdAt: Any,
    @SerializedName("email")
    val email: String,
    @SerializedName("email_verified_at")
    val emailVerifiedAt: Any,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("status")
    val status: String
)