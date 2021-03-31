package app.melon.data.resultentities

import androidx.room.Embedded
import androidx.room.Relation
import app.melon.data.entities.ANSchoolFeedEntry
import app.melon.data.entities.Feed
import java.util.Objects

class ANSchoolEntryWithFeed : EntryWithFeed<ANSchoolFeedEntry> {

    @Embedded
    override lateinit var entry: ANSchoolFeedEntry

    @Relation(parentColumn = "feed_id", entityColumn = "feed_id")
    override var relations: List<Feed> = emptyList()

    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is ANSchoolEntryWithFeed -> {
            entry == other.entry && relations == other.relations
        }
        else -> false
    }

    override fun hashCode(): Int = Objects.hash(entry, relations)
}