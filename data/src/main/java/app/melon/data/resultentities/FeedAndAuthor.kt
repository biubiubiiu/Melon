package app.melon.data.resultentities

import androidx.room.Embedded
import androidx.room.Relation
import app.melon.data.entities.Feed
import app.melon.data.entities.User

data class FeedAndAuthor(
    @Embedded val feed: Feed,
    @Relation(
        parentColumn = "author_uid",
        entityColumn = "user_id"
    )
    val author: User
): ResultEntity