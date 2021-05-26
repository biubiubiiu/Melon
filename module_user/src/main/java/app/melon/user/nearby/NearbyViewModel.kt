package app.melon.user.nearby

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import app.melon.base.framework.ObservableLoadingCounter
import app.melon.base.framework.SingleEvent
import app.melon.data.entities.User
import app.melon.location.LocateFail
import app.melon.location.LocateResult
import app.melon.location.LocateSuccess
import app.melon.location.LocationHelper
import app.melon.user.data.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


internal class NearbyViewModel @Inject constructor(
    private val locationHelper: LocationHelper,
    private val repo: UserRepository
) : ViewModel() {

    internal val locating = ObservableLoadingCounter()

    private val _locateFail = MutableLiveData<String>()
    internal val locateFail: LiveData<String> = _locateFail

    private val _dataFlow = MutableLiveData<Flow<PagingData<User>>>()
    internal val dataFlow: LiveData<Flow<PagingData<User>>> = _dataFlow

    internal fun refresh() {
        viewModelScope.launch {
            locating.addLoader()
            val result = withContext(Dispatchers.IO) {
                locationHelper.tryLocate()
            }
            when (result) {
                is LocateSuccess -> _dataFlow.postValue(fetchNearbyUser(result.longitude, result.latitude))
                is LocateFail -> _locateFail.postValue(result.errorMessage)
            }
            locating.removeLoader()
        }
    }

    private fun fetchNearbyUser(longitude: Double, latitude: Double): Flow<PagingData<User>> {
        return repo.getNearbyUser(longitude, latitude)
    }
}