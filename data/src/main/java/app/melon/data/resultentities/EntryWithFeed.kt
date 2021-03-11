package app.melon.data.resultentities

import app.melon.data.Entry
import app.melon.data.entities.Feed
import java.util.Objects

/**
 * source: https://github.com/chrisbanes/tivi
 */
interface EntryWithFeed<ET : Entry> {
    var entry: ET
    var relations: List<Feed>

    val feed: Feed
        get() {
            require(relations.size == 1)
            return relations[0]
        }

    fun generateStableId(): Long {
        return Objects.hash(entry::class.java.name, entry.feedId).toLong()
    }
}
