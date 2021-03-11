package app.melon.data.repositories

import app.melon.base.ErrorResult
import app.melon.base.Result
import app.melon.data.entities.Feed
import app.melon.data.services.FeedApiService
import app.melon.extensions.toResult
import javax.inject.Inject

class FeedDataSource @Inject constructor(
    private val service: FeedApiService
) {
    suspend fun getFeed(item: Feed): Result<Feed> {
        val id = item.feedId
        return if (id.isNotBlank()) {
            service.getFeedDetail(id).execute().toResult()
        } else {
            ErrorResult(IllegalArgumentException("ID for feed does not exist: [$item]"))
        }
    }
}