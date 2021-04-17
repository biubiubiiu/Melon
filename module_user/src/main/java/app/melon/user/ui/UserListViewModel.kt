package app.melon.user.ui

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import app.melon.data.entities.User
import app.melon.user.data.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject


class UserListViewModel @Inject constructor(
    private val repo: UserRepository
) : ViewModel() {

    fun refresh(queryType: Int, param: Bundle?): Flow<PagingData<User>> {
        return emptyFlow() // TODO
    }

    fun fetchNearbyUser(longitude: Double, latitude: Double): Flow<PagingData<User>> {
        return repo.getNearbyUser(longitude, latitude)
    }
}