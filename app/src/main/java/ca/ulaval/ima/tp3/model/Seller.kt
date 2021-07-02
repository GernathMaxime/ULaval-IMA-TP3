package ca.ulaval.ima.tp3.model

import com.google.gson.annotations.SerializedName

data class Seller (
    @SerializedName("last_name") val last_name: String,
    @SerializedName("first_name") val first_name: String,
    @SerializedName("email")  val email:String
)