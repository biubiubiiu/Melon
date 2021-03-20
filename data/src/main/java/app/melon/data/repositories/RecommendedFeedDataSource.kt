//package app.melon.data.repositories
//
//import app.melon.base.Result
//import app.melon.data.entities.Feed
//import app.melon.data.entities.RecommendFeedRemoteKey
//import app.melon.data.mappers.IDENTITY_FEED_MAPPER
//import app.melon.data.mappers.IndexedMapper
//import app.melon.data.mappers.pairMapperOf
//import app.melon.data.services.FeedApiService
//import app.melon.extensions.executeWithRetry
//import app.melon.extensions.toResult
//import javax.inject.Inject
//
//class RecommendedFeedDataSource @Inject constructor(
//    private val service: FeedApiService
//) {
//    private val entryMapper = object : IndexedMapper<Feed, RecommendFeedRemoteKey> {
//        override suspend fun map(index: Int, from: Feed): RecommendFeedRemoteKey {
//            return RecommendFeedRemoteKey(feedId = 0, page = 0, pageOrder = index)
//        }
//    }
//
//    private val resultsMapper = pairMapperOf(IDENTITY_FEED_MAPPER, entryMapper)
//
//    suspend operator fun invoke(
//        time: Long,
//        page: Int,
//        pageSize: Int
//    ): Result<List<Pair<Feed, RecommendFeedRemoteKey>>> {
//        return service.fetchRecommendList(time, page, pageSize)
//            .executeWithRetry()
//            .toResult(resultsMapper)
//    }
//}