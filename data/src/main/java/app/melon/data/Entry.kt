package app.melon.data

import app.melon.data.entities.MelonEntity

/**
 * source: https://github.com/chrisbanes/tivi
 */
interface Entry : MelonEntity {
    val feedId: String
}

interface PaginatedEntry : Entry {
    val page: Int
    val pageOrder: Int
}

interface FeedEntry : PaginatedEntry {
    val feedType: String
}