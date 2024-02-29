package com.jpdev.livestockproject.domain.model

import com.google.gson.annotations.SerializedName

data class DeathDetails (
    @SerializedName("deathDate") var deathDate:String = "",
    @SerializedName("deathCause") var deathCause:String = "",
    @SerializedName("deathDescription") var deathDescription:String = ""
)