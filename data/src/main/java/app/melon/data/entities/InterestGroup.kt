package app.melon.data.entities

data class InterestGroup(
    override val id: Long,
    val name: String,
    val icon: String,
    val members: Long,
    val description: String
) : MelonEntity
