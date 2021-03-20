package app.melon.data.entities

interface MelonEntity {
    val id: Long
}

interface FeedEntity : MelonEntity {
    val type: String
    val feedId: String // for diffing stuff
}