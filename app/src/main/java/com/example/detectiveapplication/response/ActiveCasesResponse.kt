package com.example.detectiveapplication.response

import com.example.detectiveapplication.dto.cases.Case
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ActiveCasesResponse(
    @SerializedName("statues") var statues: Boolean,
    @SerializedName("code") var code: Int,
    @SerializedName("data") var data: Data
)

@Serializable
data class Data(
    @SerializedName("current_page") var current_page: Int? = null,
    @SerializedName("first_page_url") var firstPageUrl: String? = null,
    @SerializedName("from") var from: Int? = null,
    @SerializedName("last_page") var lastPage: Int? = null,
    @SerializedName("last_page_url") var lastPageUrl: String? = null,
    @SerializedName("next_page_url") var nextPageUrl: String? = null,
    @SerializedName("path") var path: String? = null,
    @SerializedName("per_page") var perPage: Int? = null,
    @SerializedName("prev_page_url") var prevPageUrl: String? = null,
    @SerializedName("to") var to: Int? = null,
    @SerializedName("total") var total: Int? = null,
    @SerializedName("data") var cases: List<Case> = emptyList()
)