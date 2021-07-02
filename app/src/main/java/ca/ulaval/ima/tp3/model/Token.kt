package ca.ulaval.ima.tp3.model

import com.google.gson.annotations.SerializedName

data class Token (
        @SerializedName("token") val token: String
)