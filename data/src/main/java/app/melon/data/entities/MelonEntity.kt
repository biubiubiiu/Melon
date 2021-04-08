package app.melon.data.entities

import java.io.Serializable

interface MelonEntity: Serializable {
    val id: Long
}

interface FeedEntity : MelonEntity {
    val type: String
    val feedId: String // for diffing stuff
}