package com.example.detectiveapplication.dto.search_response


import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: SearchData,
    @SerializedName("statues")
    val statues: Boolean
)

data class SearchData(
    @SerializedName("current_page")
    val currentPage: Int,
    @SerializedName("data")
    val `data`: List<SearchDataX>,
    @SerializedName("first_page_url")
    val firstPageUrl: String,
    @SerializedName("from")
    val from: Int,
    @SerializedName("last_page")
    val lastPage: Int,
    @SerializedName("last_page_url")
    val lastPageUrl: String,
    @SerializedName("next_page_url")
    val nextPageUrl: Any,
    @SerializedName("path")
    val path: String,
    @SerializedName("per_page")
    val perPage: String,
    @SerializedName("prev_page_url")
    val prevPageUrl: Any,
    @SerializedName("to")
    val to: Int,
    @SerializedName("total")
    val total: Int
)

data class SearchDataX(
    @SerializedName("age")
    val age: Any,
    @SerializedName("city")
    val city: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("kidnap_status")
    val kidnapStatus: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("other_info")
    val otherInfo: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("sub_city")
    val subCity: String
)