package com.example.detectiveapplication.dto.login

import com.google.gson.annotations.SerializedName

data class Error(
    @SerializedName("email")
    val email: List<String>? = null,
    @SerializedName("password")
    val password: List<String>? = null
)