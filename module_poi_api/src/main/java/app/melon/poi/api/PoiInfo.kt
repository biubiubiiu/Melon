package app.melon.poi.api

import java.io.Serializable

data class PoiInfo(
    val id: String,
    val longitude: Double,
    val latitude: Double
) : Serializable