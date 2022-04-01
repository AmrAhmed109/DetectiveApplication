package com.example.detectiveapplication.dto.auth_response.login


import com.google.gson.annotations.SerializedName

data class UserData(
    @SerializedName("created_at")
    val createdAt: Any,
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String
)