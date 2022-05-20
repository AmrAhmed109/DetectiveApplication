package com.example.detectiveapplication.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CasesSearchRequest(
    val fromDate: String? = null,
    val toDate: String? = null,
    val age: Int? = null,
    val city: String? = null,
    @SerialName("sub_city")
    val subCity: String? = null,
    val name: String? = null,
)
