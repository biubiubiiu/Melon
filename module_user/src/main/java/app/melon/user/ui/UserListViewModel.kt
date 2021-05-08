package app.melon.user.ui

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import app.melon.data.entities.User
import app.melon.user.data.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject


internal class UserListViewModel @Inject constructor(
    private val repo: UserRepository
) : ViewModel() {

    internal fun refresh(): Flow<PagingData<User>> {
        return emptyFlow() // TODO
    }

    internal fun fetchNearbyUser(longitude: Double, latitude: Double): Flow<PagingData<User>> {
        return repo.getNearbyUser(longitude, latitude)
    }
}