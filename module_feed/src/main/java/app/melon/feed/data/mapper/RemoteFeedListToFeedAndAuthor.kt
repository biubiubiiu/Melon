package app.melon.feed.data.mapper

import app.melon.data.entities.Feed
import app.melon.data.entities.User
import app.melon.data.entities.isValidGender
import app.melon.data.remote.toLocation
import app.melon.data.remote.toPoiInfo
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
            authorUid = from.user?.id,
            title = from.title,
            content = from.content,
            photos = from.photos,
            poiInfo = from.location?.toPoiInfo(),
            isFavored = from.isFavor,
            isCollected = from.isCollected,
            postTime = from.postTime,
            replyCount = from.replyCount,
            favouriteCount = from.favorCount
        )
        val user = User(
            id = from.user?.id ?: "",
            username = from.user?.username,
            customId = from.user?.customId,
            gender = if (from.user?.gender?.isValidGender() == true) from.user.gender else null,
            age = from.user?.age,
            school = from.user?.school,
            location = from.user?.lastLocation.toLocation(),
            avatarUrl = from.user?.avatarUrl
        )
        return FeedAndAuthor(feed, user)
    }
}