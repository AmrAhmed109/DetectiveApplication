package com.example.detectiveapplication.response

import com.example.detectiveapplication.dto.cases.Case
import com.google.gson.annotations.SerializedName

data class ActiveCasesResponse(
    @SerializedName("statues") var statues: Boolean? = null,
    @SerializedName("code") var code: Int? = null,
    @SerializedName("data") var cases: List<Case> = emptyList()
)