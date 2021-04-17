package app.melon.feed.data.mapper

import app.melon.data.entities.Feed
import app.melon.data.entities.MALE
import app.melon.data.entities.User
import app.melon.data.entities.isValidGender
import app.melon.data.remote.toLocation
import app.melon.data.resultentities.FeedAndAuthor
import app.melon.feed.data.remote.FeedListItemResponse
import app.melon.util.mappers.Mapper
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RemoteFeedListToFeedAndAuthor @Inject constructor() : Mapper<FeedListItemResponse, FeedAndAuthor> {
    override suspend fun map(from: FeedListItemResponse): FeedAndAuthor {
        val feed = Feed(
            id = from.id,
            authorUid = from.user.id,
            title = from.title ?: "",
            content = from.content,
            photos = from.photos ?: emptyList(),
            postTime = from.postTime ?: "",
            replyCount = from.replyCount ?: 0L,
            favouriteCount = from.favorCount ?: 0L
        )
        val user = User(
            id = from.user.id,
            username = from.user.username,
            gender = if (from.user.gender?.isValidGender() == true) from.user.gender else MALE,
            age = from.user.age ?: 0,
            school = from.user.school,
            location = from.user.lastLocation.toLocation(),
            avatarUrl = from.user.avatarUrl ?: ""
        )
        return FeedAndAuthor(feed, user)
    }
}