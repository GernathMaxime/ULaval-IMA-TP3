package ca.ulaval.ima.tp3.model

import com.google.gson.annotations.SerializedName

data class Brand (
    @SerializedName("id") val id:Int,
    @SerializedName("name")  val name:String
)