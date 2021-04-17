package app.melon.data.entities

import java.io.Serializable

data class Location(
    val longitude: Double,
    val latitude: Double
): Serializable {
    override fun toString(): String {
        return "($longitude,$latitude)"
    }
}