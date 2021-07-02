package ca.ulaval.ima.tp3.model

import com.google.gson.annotations.SerializedName

data class SeachOffer(
    @SerializedName("id") val id:Int,
    @SerializedName("model") val model: Model,
    @SerializedName("image") val image: String,
    @SerializedName("year")  val year:Int,
    @SerializedName("from_owner")  val from_owner:Boolean,
    @SerializedName("kilometers")  val kilometers:Int,
    @SerializedName("price")  val price:Int,
    @SerializedName("created")  val created:String
)