package com.example.detectiveapplication.dto.notification


import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<NotificationFeed>,
    @SerializedName("statues")
    val statues: Boolean
)

data class NotificationFeed(
    @SerializedName("body")
    val body: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: Any,
    @SerializedName("readable")
    val readable: Int,
    @SerializedName("refreance_id")
    val refreanceId: Int,
    @SerializedName("tittle")
    val tittle: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user_id")
    val userId: Int
)