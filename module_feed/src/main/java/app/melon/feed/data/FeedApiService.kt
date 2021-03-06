package app.melon.feed.data

import app.melon.base.network.ApiBaseException
import app.melon.base.network.MelonApiService
import app.melon.data.constants.FeedPageType
import app.melon.feed.data.remote.FeedDetailResponse
import app.melon.feed.data.remote.FeedListItemResponse
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FeedApiService @Inject constructor(
    private val api: FeedApi
) : MelonApiService() {

    suspend fun list(
        time: String,
        @FeedPageType type: Int,
        page: Int,
        pageSize: Int
    ): Result<List<FeedListItemResponse>> {
        return call {
            api.list(time, type, page, pageSize)
        }
    }

    suspend fun detail(
        id: String
    ): Result<FeedDetailResponse> {
        return call {
            api.detail(id)
        }
    }

    suspend fun feedsFromUser(
        id: String,
        page: Int,
        pageSize: Int,
        isAnonymous: Boolean
    ): Result<List<FeedListItemResponse>> {
        val type = if (isAnonymous) 1 else 0
        return call {
            api.feedsFromUser(id, page, pageSize, type)
        }
    }

    suspend fun favorsOfUser(
        id: String,
        page: Int,
        pageSize: Int
    ): Result<List<FeedListItemResponse>> {
        return call {
            api.favorsOfUser(id, page, pageSize)
        }
    }

    suspend fun bookmarksOfUser(
        id: String,
        page: Int,
        pageSize: Int
    ): Result<List<FeedListItemResponse>> {
        return call {
            api.bookmarksOfUser(id, page, pageSize)
        }
    }

    suspend fun poiFeeds(
        id: String,
        page: Int,
        pageSize: Int
    ): Result<List<FeedListItemResponse>> {
        return call {
            api.poiFeeds(id, page, pageSize)
        }
    }

    suspend fun postFeed(
        content: String,
        images: List<String>,
        location: String
    ): Result<Boolean> {
        return call {
            api.postFeed(content, images, location)
        }
    }

    suspend fun collect(
        id: String
    ): Result<Boolean> {
        return call {
            api.collect(id)
        }
    }

    // TODO define custom error
    override fun mapApiThrowable(message: String, code: Int): ApiBaseException {
        return super.mapApiThrowable(message, code)
    }
}