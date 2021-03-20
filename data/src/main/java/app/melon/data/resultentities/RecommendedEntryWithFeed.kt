package app.melon.data.resultentities

import androidx.room.Embedded
import androidx.room.Relation
import app.melon.data.entities.Feed
import app.melon.data.entities.RecommendedFeedEntry
import java.util.Objects

class RecommendedEntryWithFeed : EntryWithFeed<RecommendedFeedEntry> {

    @Embedded
    override lateinit var entry: RecommendedFeedEntry

    @Relation(parentColumn = "feed_id", entityColumn = "feed_id")
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