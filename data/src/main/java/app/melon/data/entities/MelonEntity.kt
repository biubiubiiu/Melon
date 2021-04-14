package app.melon.data.entities

import java.io.Serializable

interface MelonEntity : Serializable {
    val id: String // for diffing stuff
}