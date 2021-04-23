package app.melon.feed.interactors

import android.os.Bundle
import androidx.paging.PagingData
import app.melon.base.domain.SubjectInteractor
import app.melon.data.constants.MY_ANONYMOUS_POST
import app.melon.data.constants.MY_FAVORITE_POST
import app.melon.data.constants.MY_POST
import app.melon.data.resultentities.FeedAndAuthor
import app.melon.feed.data.FeedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class UpdateFeedList @Inject constructor(
    private val repo: FeedRepository
) : SubjectInteractor<UpdateFeedList.Params, PagingData<FeedAndAuthor>>() {

    override fun createObservable(params: Params): Flow<PagingData<FeedAndAuthor>> {
        return when (params.queryType) {
            MY_POST, MY_ANONYMOUS_POST, MY_FAVORITE_POST -> {
                val fakeUid = "6" // TODO
                repo.getFeedsFromUser(fakeUid)
            }
            else -> {
                repo.getFeedList(params.timestamp, params.queryType)
            }
        }
    }

    data class Params(
        val queryType: Int,
        val timestamp: String,
        val param: Bundle?
    )
}