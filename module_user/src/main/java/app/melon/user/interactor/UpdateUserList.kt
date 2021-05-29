package app.melon.user.interactor

import androidx.paging.PagingData
import app.melon.base.domain.SubjectInteractor
import app.melon.data.constants.FOLLOWERS_USER
import app.melon.data.constants.FOLLOWING_USER
import app.melon.data.entities.User
import app.melon.user.data.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject


internal class UpdateUserList @Inject constructor(
    private val repo: UserRepository
) : SubjectInteractor<UpdateUserList.Params, PagingData<User>>() {

    override fun createObservable(params: Params): Flow<PagingData<User>> {
        return when (params.queryType) {
            FOLLOWERS_USER -> repo.getFollowers(params.uid)
            FOLLOWING_USER -> repo.getFollowing(params.uid)
            else -> emptyFlow()
        }
    }

    data class Params(
        val queryType: Int,
        val uid: String
    )
}