package com.example.detectiveapplication.dto.followedCases


import com.google.gson.annotations.SerializedName

class FollowedCasesResponse : ArrayList<FollowedCasesItem>()
data class FollowedCasesItem(
    @SerializedName("age")
    val age: Any,
    @SerializedName("birth_image")
    val birthImage: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("id_number")
    val idNumber: Any,
    @SerializedName("image")
    val image: String,
    @SerializedName("kidnap_date")
    val kidnapDate: String,
    @SerializedName("kidnap_status")
    val kidnapStatus: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("other_info")
    val otherInfo: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("sub_city")
    val subCity: String,
    @SerializedName("user_id")
    val userId: Int
)