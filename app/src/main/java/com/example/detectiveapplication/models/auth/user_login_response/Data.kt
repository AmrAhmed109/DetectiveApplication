package com.example.detectiveapplication.models.auth.user_login_response


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("user_data")
    val userData: UserData
)