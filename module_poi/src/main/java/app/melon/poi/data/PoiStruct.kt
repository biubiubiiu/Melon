package app.melon.poi.data

import app.melon.location.SimplifiedLocation

data class PoiStruct(
    val name: String,
    val district: String,
    val type: String,
    val address: String,
    val location: SimplifiedLocation
)