package app.melon.data.entities

import androidx.room.PrimaryKey

data class User(
    @PrimaryKey val id: String = "",
    val email: String = "",
    val username: String = "",
    val school: String = "",
    val location: String = "",
    val description: String = "",
    val photos: List<String> = emptyList()
)