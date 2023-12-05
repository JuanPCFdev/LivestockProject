package com.jpdev.livestockproject.domain.model

import com.google.gson.annotations.SerializedName

data class Buyer(//Comprador
    @SerializedName("idBuyer") var idBuyer:Int = 0,
    @SerializedName("nameBuyer") var nameBuyer: String = "",
    @SerializedName("telBuyer") var telBuyer: Int = 0
)
