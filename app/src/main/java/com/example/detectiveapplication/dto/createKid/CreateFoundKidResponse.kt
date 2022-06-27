package com.example.detectiveapplication.dto.createKid


import com.google.gson.annotations.SerializedName

data class CreateFoundKidResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: StrangerKidData,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("error")
    val error: FoundError
)

data class FoundError(
    @SerializedName("city")
    val city: List<String>,
    @SerializedName("sub_city")
    val subCity: List<String>,
    @SerializedName("image")
    val image: List<String>,
    @SerializedName("name")
    val name: List<String>,
    @SerializedName("other_info")
    val otherInfo: List<String>,
    @SerializedName("parent_address")
    val parentAddress: List<String>,
    @SerializedName("parent_name")
    val parentName: List<String>,
    @SerializedName("parent_national_id")
    val parentNationalId: List<String>,
    @SerializedName("parent_other_info")
    val parentOtherInfo: List<String>,
    @SerializedName("parent_phone_number")
    val parentPhoneNumber: List<String>,
    @SerializedName("status")
    val status: List<String>,
)
data class StrangerKidData(
    @SerializedName("city")
    val city: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("other_info")
    val otherInfo: String,
    @SerializedName("recognation")
    val recognation: List<Any>,
    @SerializedName("status")
    val status: String,
    @SerializedName("sub_city")
    val subCity: String,
    @SerializedName("user_id")
    val userId: Int
)