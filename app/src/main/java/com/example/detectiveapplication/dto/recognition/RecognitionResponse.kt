package com.example.detectiveapplication.dto.recognition


import com.google.gson.annotations.SerializedName

data class RecognitionResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<RecognitionData>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)



data class RecognitionData(
    @SerializedName("age")
    val age: Int,
    @SerializedName("birth_image")
    val birthImage: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("id_number")
    val idNumber: Long,
    @SerializedName("image")
    val image: String,
    @SerializedName("item")
    val item: Int,
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