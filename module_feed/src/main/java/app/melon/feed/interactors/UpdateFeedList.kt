package app.melon.feed.interactors

import android.os.Bundle
import androidx.paging.PagingData
import app.melon.account.api.UserManager
import app.melon.base.domain.SubjectInteractor
import app.melon.data.constants.BOOKMARK_FEEDS
import app.melon.data.constants.MY_ANONYMOUS_POST
import app.melon.data.constants.MY_FAVORITE_POST
import app.melon.data.constants.MY_POST
import app.melon.data.resultentities.FeedAndAuthor
import app.melon.feed.data.FeedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject


class UpdateFeedList @Inject constructor(
    private val repo: FeedRepository,
    private val userManager: UserManager
) : SubjectInteractor<UpdateFeedList.Params, PagingData<FeedAndAuthor>>() {

    override fun createObservable(params: Params): Flow<PagingData<FeedAndAuthor>> {
        val user = userManager.user ?: return emptyFlow()
        return when (params.queryType) {
            MY_POST -> repo.getFeedsFromUser(user.id, isAnonymous = false)
            MY_ANONYMOUS_POST -> repo.getFeedsFromUser(user.id, isAnonymous = true)
            MY_FAVORITE_POST -> repo.getFavorsOfUser(user.id)
            BOOKMARK_FEEDS -> repo.getBookmarksOfUser(user.id)
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