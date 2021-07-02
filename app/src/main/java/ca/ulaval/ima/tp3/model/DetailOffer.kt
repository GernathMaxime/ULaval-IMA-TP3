package ca.ulaval.ima.tp3.model

import com.google.gson.annotations.SerializedName

data class DetailOffer(
    @SerializedName("id") val id:Int,
    @SerializedName("model") val model: Model,
    @SerializedName("image") val image: String,
    @SerializedName("description") val description: String,
    @SerializedName("seller") val seller: Seller,
    @SerializedName("year")  val year:Int,
    @SerializedName("from_owner")  val from_owner:Boolean,
    @SerializedName("kilometers")  val kilometers:Int,
    @SerializedName("transmission")  val transmission:String,
    @SerializedName("price")  val price:Int,
    @SerializedName("created")  val created:String
)