package com.example.detectiveapplication.dto.login


import com.google.gson.annotations.SerializedName

data class UserLoginResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val data: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("error")
    val error: Error
)

