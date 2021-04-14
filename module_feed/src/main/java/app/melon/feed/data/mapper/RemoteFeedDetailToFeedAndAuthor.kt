package app.melon.feed.data.mapper

import app.melon.data.entities.Feed
import app.melon.data.entities.MALE
import app.melon.data.entities.User
import app.melon.data.entities.isValidGender
import app.melon.feed.data.remote.FeedDetailResponse
import app.melon.util.mappers.Mapper
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RemoteFeedDetailToFeedAndAuthor @Inject constructor() : Mapper<FeedDetailResponse, Pair<Feed, User>> {
    override suspend fun map(from: FeedDetailResponse): Pair<Feed, User> {
        val feed = Feed(
            id = from.id,
            authorUid = from.user.id,
            content = from.content,
            photos = from.photos ?: emptyList(),
            postTime = from.postTime ?: "",
            replyCount = from.replyCount ?: 0L,
            favouriteCount = from.favouriteCount ?: 0L
        )
        val user = User(
            id = from.user.id,
            username = from.user.username,
            gender = if (from.user.gender?.isValidGender() == true) from.user.gender else MALE,
            age = from.user.age ?: 0,
            school = from.user.school,
            location = from.user.lastLocation ?: "",
            avatarUrl = from.user.avatarUrl ?: ""
        )
        return feed to user
    }
}