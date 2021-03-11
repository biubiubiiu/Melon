package app.melon.data.resultentities

import androidx.room.Embedded
import androidx.room.Relation
import app.melon.data.entities.Feed
import app.melon.data.entities.FollowingFeedEntry
import java.util.Objects

class FollowingEntryWithFeed : EntryWithFeed<FollowingFeedEntry> {

    @Embedded
    override lateinit var entry: FollowingFeedEntry

    @Relation(parentColumn = "feed_id", entityColumn = "id")
    override var relations: List<Feed> = emptyList()

    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is FollowingEntryWithFeed -> {
            entry == other.entry && relations == other.relations
        }
        else -> false
    }

    override fun hashCode(): Int = Objects.hash(entry, relations)
}