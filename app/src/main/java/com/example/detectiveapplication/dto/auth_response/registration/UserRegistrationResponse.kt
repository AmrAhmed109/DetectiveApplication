package com.example.detectiveapplication.dto.auth_response.registration


import com.google.gson.annotations.SerializedName

data class UserRegistrationResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("error")
    val error: List<String>,
    @SerializedName("message")
    val message: String,
    @SerializedName("statues")
    val statues: Boolean
)