package app.melon.data

import app.melon.data.entities.MelonEntity

/**
 * source: https://github.com/chrisbanes/tivi
 */
interface Entry : MelonEntity {
    val feedId: Long
}

interface PaginatedEntry : Entry {
    val page: Int
}