package app.melon.home.nearby

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.melon.data.entities.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NearbyViewModel @Inject constructor(
    private val repository: NearbyRepository
) : ViewModel() {

    fun getStream(): Flow<PagingData<User>> {
        return repository.getStream().cachedIn(viewModelScope)
    }
}