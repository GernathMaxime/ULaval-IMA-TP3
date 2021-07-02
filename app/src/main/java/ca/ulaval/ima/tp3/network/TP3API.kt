package ca.ulaval.ima.tp3.network

import ca.ulaval.ima.tp3.BuildConfig
import ca.ulaval.ima.tp3.model.*
import com.google.gson.annotations.SerializedName
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface TP3API {
    companion object{
        const val API_V1 = "/api/v1/"
        const val BASE_URL: String = BuildConfig.BASE_URL
    }

    @GET(API_V1 + "brand/")
    fun listbrand(): Call<ContentResponse<List<Brand>>>

    @GET(API_V1 + "brand/{id}/models/")
    fun listbrandmodel(@Path("id") id: Int): Call<ContentResponse<List<Model>>>

    @GET(API_V1 + "model/")
    fun listmodel(): Call<ContentResponse<List<Model>>>

    @GET(API_V1 + "offer/search/")
    fun listsearch(@Query("model") model: Int, @Query("brand") brand: Int): Call<ContentResponse<List<SeachOffer>>>

    @POST(API_V1 + "offer/add/")
    fun addOffer(@Body Body: Map<String, String>, @Header("Authorization") token: String): Call<ContentResponse<DetailOffer>>

    @GET(API_V1 + "offer/mine/")
    fun myOffer(@Header("Authorization") token: String): Call<ContentResponse<List<SeachOffer>>>

    @GET(API_V1 + "offer/{id}/details/")
    fun detailoffer(@Path("id") id: Int): Call<ContentResponse<DetailOffer>>

    @Headers("Content-Type: application/json")
    @POST(API_V1 + "account/login/")
    fun login(@Body Body: Map<String, String>): Call<ContentResponse<Token>>

    @GET(API_V1 + "account/me/")
    fun getaccount(@Header("Authorization") token: String): Call<ContentResponse<Account>>

    data class ContentResponse<T> (
        @SerializedName("content") val content : T,
        @SerializedName("meta") val meta: Meta,
        @SerializedName("error") var error: Error
    )

    data class Error (
        @SerializedName("display") val displayMessage: String
    )

    data class Meta (
        @SerializedName("status_code") val statusCode: Int
    )
}