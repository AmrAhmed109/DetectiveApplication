package com.example.detectiveapplication.dto.followStatues


import com.google.gson.annotations.SerializedName

data class FollowStatuesSaveResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)