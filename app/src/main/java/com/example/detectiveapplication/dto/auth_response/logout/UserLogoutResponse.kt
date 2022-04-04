package com.example.detectiveapplication.dto.auth_response.logout


import com.google.gson.annotations.SerializedName

data class UserLogoutResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)