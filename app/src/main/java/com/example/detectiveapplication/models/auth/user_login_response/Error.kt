package com.example.detectiveapplication.models.auth.user_login_response
import com.google.gson.annotations.SerializedName

data class Error(
    @SerializedName("email")
    val email: List<String>,
    @SerializedName("password")
    val password: List<String>
)