package com.example.detectiveapplication.dto.cases

import com.google.gson.annotations.SerializedName

data class Case(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("other_info") var otherInfo: String? = null,
    @SerializedName("age") var age: Int? = null,
    @SerializedName("city") var city: String? = null,
    @SerializedName("sub_city") var subCity: String? = null
)