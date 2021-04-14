package app.melon.data.entities

/**
 * source: https://github.com/chrisbanes/tivi
 */
interface Entry {
    val id: Long // auto generate by database
    val relatedId: String
}

interface PaginatedEntry : Entry {
    val page: Int
    val pageOrder: Int
}