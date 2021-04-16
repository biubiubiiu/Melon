package app.melon.user.ui

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import app.melon.data.constants.NEARBY_USER
import app.melon.data.entities.User
import app.melon.user.data.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject


class UserListViewModel @Inject constructor(
    private val repo: UserRepository
) : ViewModel() {

    fun refresh(queryType: Int, param: Bundle?): Flow<PagingData<User>> {
        return when (queryType) {
            NEARBY_USER -> repo.getNearbyUser(
                1f, // TODO
                1f
            )
            else -> emptyFlow()
        }
    }
}