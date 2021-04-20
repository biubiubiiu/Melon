package app.melon.data.entities

import java.io.Serializable

data class PoiInfo(
    val poiId: String,
    val poiName: String,
    val longitude: Double,
    val latitude: Double
) : Serializable {
    override fun toString(): String {
        return "($poiId,$poiName,$longitude,$latitude)"
    }
}