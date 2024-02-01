package com.jpdev.livestockproject.domain.model

import com.google.gson.annotations.SerializedName

data class BreedingPerformance (
    @SerializedName("PBDateBorn") var PBDate: String = "", //Fecha en la que nacio la cria
    @SerializedName("PBDateInsemination") var PBDateInsemination:String ="", //Fecha de inseminación
    @SerializedName("PBInitialWeight") var PBInitialWeight:Int = 0, //Peso inicial del animal al nacer
    @SerializedName("PBSick") var PBSick:Boolean = false, //Nacio enfermo?
    @SerializedName("PBDeath") var PBDeath:Boolean = false, //Murio durante la gestacion?
    @SerializedName("PBDiet") var PBDiet:String = "" //Dieta de la madre
)