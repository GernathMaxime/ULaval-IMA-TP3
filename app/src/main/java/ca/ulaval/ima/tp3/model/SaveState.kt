package ca.ulaval.ima.tp3.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class SaveState(var id: Int, var km: String?, var year: Int?, var transmission: Int?, var price: String?, var owner: Boolean?, var brand: Int?, var model: Int?, var carName: String?): Parcelable
