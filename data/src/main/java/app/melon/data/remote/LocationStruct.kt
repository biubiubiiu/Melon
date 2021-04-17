package app.melon.data.remote

import app.melon.data.entities.Location
import com.google.gson.annotations.SerializedName

data class LocationStruct(
    @SerializedName("point") val location: String? = null,
    @SerializedName("desc") val poiName: String? = null,
    @SerializedName("precision") val accuracy: Float? = null
)

fun LocationStruct?.toLocation(): Location? {
    val str = this?.location ?: return null

    val start = str.indexOfFirst { it == '(' }
    val end = str.indexOfLast { it == ')' }
    if (start == -1 || end == -1) return null

    val data = str.substring(start + 1, end).split(' ', limit = 2)
    val longitude = data.getOrNull(0)?.toDoubleOrNull() ?: return null
    val latitude = data.getOrNull(1)?.toDoubleOrNull() ?: return null
    return Location(longitude, latitude)
}