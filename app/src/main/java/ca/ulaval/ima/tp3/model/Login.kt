package ca.ulaval.ima.tp3.model

import com.google.gson.annotations.SerializedName

data class Login (
        @SerializedName("email") val email:String,
        @SerializedName("ni") val ni: String
)