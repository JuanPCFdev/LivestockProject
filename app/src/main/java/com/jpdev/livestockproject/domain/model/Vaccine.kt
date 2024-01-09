package com.jpdev.livestockproject.domain.model

import com.google.gson.annotations.SerializedName

data class Vaccine (
    @SerializedName("idVaccine") var idVaccine: Int = 0, //id de la vacuna
    @SerializedName("vaccineName") var vacineName: String = "", //Nombre de la vacuna
    @SerializedName("vaccineCost") var vaccineCost:Double = 0.0, //Costo de la vacuna
    @SerializedName("date") var date: String = "", //Fecha de aplicacion
    @SerializedName("supplier") var supplier:String = "" //Quien la suministro
)