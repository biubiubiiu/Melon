package app.melon.data.resultentities

import androidx.room.Embedded
import androidx.room.Relation
import app.melon.data.entities.Feed
import app.melon.data.entities.FeedEntry


data class EntryWithFeedAndAuthor(

    @Embedded val entry: FeedEntry,
    @Relation(
        parentColumn = "feed_id",
        entityColumn = "feed_id",
        entity = Feed::class
    )
    val compoundItem: FeedAndAuthor
)