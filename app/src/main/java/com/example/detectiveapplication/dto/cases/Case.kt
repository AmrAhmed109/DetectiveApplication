package com.example.detectiveapplication.dto.cases

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Case(
    @SerialName("id") var id: Int? = null,
    @SerialName("name") var name: String? = null,
    @SerialName("image") var image: String? = null,
    @SerialName("status") var status: String? = null,
    @SerialName("kidnap_status") var kidnap_status: String? = null,
    @SerialName("other_info") var otherInfo: String? = null,
    @SerialName("age") var age: Int? = null,
    @SerialName("city") var city: String? = null,
    @SerialName("sub_city") var subCity: String? = null
)