package app.melon.data.dto

import app.melon.data.entities.Feed
import com.google.gson.annotations.SerializedName

/**
 * Data class to hold feed response from [FeedApiService.fetchFeedList] calls.
 */
abstract class PaginatedResponse<Feed>(
    @SerializedName("page") val page: Int = 0,
    @SerializedName("total_page") val totalPage: Int = 0,
    @SerializedName("items") val items: List<Feed> = emptyList()
)

class RecommendedFeedResponse : PaginatedResponse<Feed>()

class FollowingFeedResponse : PaginatedResponse<Feed>()