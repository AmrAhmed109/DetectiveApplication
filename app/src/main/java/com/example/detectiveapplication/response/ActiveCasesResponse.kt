package com.example.detectiveapplication.response

import com.example.detectiveapplication.dto.cases.Case
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActiveCasesResponse(
    @SerialName("statues") var statues: Boolean,
    @SerialName("code") var code: Int,
    @SerialName("data") var data: Data
)

@Serializable
data class Data(
    @SerialName("current_page") var current_page: Int? = null,
    @SerialName("first_page_url") var firstPageUrl: String? = null,
    @SerialName("from") var from: Int? = null,
    @SerialName("last_page") var lastPage: Int? = null,
    @SerialName("last_page_url") var lastPageUrl: String? = null,
    @SerialName("next_page_url") var nextPageUrl: String? = null,
    @SerialName("path") var path: String? = null,
    @SerialName("per_page") var perPage: Int? = null,
    @SerialName("prev_page_url") var prevPageUrl: String? = null,
    @SerialName("to") var to: Int? = null,
    @SerialName("total") var total: Int? = null,
    @SerialName("data") var cases: List<Case>
)