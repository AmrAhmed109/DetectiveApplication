package com.example.detectiveapplication.dto.reset_password


import com.google.gson.annotations.SerializedName

data class CodeVerificationResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)