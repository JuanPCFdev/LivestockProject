package com.jpdev.livestockproject.domain.model

import com.google.gson.annotations.SerializedName

data class LiftingPerformance (
    @SerializedName("PLDate") var PLDate:String = "", //Fecha de toma del muestreo
    @SerializedName("PLWeight") var PLWeight:Int = 0, //Peso en el muestreo
    @SerializedName("PLDiet") var PLDiet:String = "" //Dieta que se le dio a la vaca desde el ultimo muestreo
)