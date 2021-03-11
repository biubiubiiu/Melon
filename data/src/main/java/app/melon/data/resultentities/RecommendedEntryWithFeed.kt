package app.melon.data.resultentities

import androidx.room.Embedded
import androidx.room.Relation
import app.melon.data.entities.Feed
import app.melon.data.entities.RecommendFeedEntry
import java.util.Objects

class RecommendedEntryWithFeed : EntryWithFeed<RecommendFeedEntry> {

    @Embedded
    override lateinit var entry: RecommendFeedEntry

    @Relation(parentColumn = "feed_id", entityColumn = "id")
    override var relations: List<Feed> = emptyList()

    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is RecommendedEntryWithFeed -> {
            entry == other.entry && relations == other.relations
        }
        else -> false
    }

    override fun hashCode(): Int = Objects.hash(entry, relations)
}