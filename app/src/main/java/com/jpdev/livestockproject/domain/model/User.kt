package com.jpdev.livestockproject.domain.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("userId") var userId: Int = 0,
    @SerializedName("username") var name: String = "",
    @SerializedName("password") var password: String = "",
    @SerializedName("phone") var phone: String = "",
    @SerializedName("farms") var farms: MutableList<Farm> = mutableListOf()
    )