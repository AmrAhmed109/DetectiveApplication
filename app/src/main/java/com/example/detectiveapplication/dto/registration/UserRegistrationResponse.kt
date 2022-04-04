package com.example.detectiveapplication.dto.registration


import com.google.gson.annotations.SerializedName

data class UserRegistrationResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("error")
    val error: List<String>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("status")
    val status: Boolean
)