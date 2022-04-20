package com.example.detectiveapplication.dto.reset_password


import com.google.gson.annotations.SerializedName

data class ForgetPasswordResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("error")
    val error: List<String>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)