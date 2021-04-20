package app.melon.feed.data.mapper

import app.melon.data.entities.Feed
import app.melon.data.entities.User
import app.melon.data.entities.isValidGender
import app.melon.data.remote.toLocation
import app.melon.data.remote.toPoiInfo
import app.melon.feed.data.remote.FeedDetailResponse
import app.melon.util.mappers.Mapper
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RemoteFeedDetailToFeedAndAuthor @Inject constructor() : Mapper<FeedDetailResponse, Pair<Feed, User>> {
    override suspend fun map(from: FeedDetailResponse): Pair<Feed, User> {
        val feed = Feed(
            id = from.id,
            authorUid = from.user?.id,
            content = from.content,
            photos = from.photos,
            poiInfo = from.location?.toPoiInfo(),
            postTime = from.postTime,
            replyCount = from.replyCount,
            favouriteCount = from.favouriteCount
        )
        val user = User(
            id = from.user?.id ?: "",
            username = from.user?.username,
            gender = if (from.user?.gender?.isValidGender() == true) from.user.gender else null,
            age = from.user?.age,
            school = from.user?.school,
            location = from.user?.lastLocation.toLocation(),
            avatarUrl = from.user?.avatarUrl
        )
        return feed to user
    }
}