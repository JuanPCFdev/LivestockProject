package com.jpdev.livestockproject.domain.model

import com.google.gson.annotations.SerializedName

data class Insemination(
    @SerializedName("inseminationDate") var inseminationDate: String = "",
    @SerializedName("descOfInsemination") var descOfInsemination: String = "",
    @SerializedName("spermOrigin") var spermOrigin: String = ""
)
