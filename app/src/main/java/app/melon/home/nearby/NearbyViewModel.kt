package app.melon.home.nearby

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.melon.location.LocateResult
import app.melon.location.LocationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NearbyViewModel @Inject constructor(
    private val locationHelper: LocationHelper
) : ViewModel() {

    private val _locateResult = MutableLiveData<LocateResult>()
    val locateResult: LiveData<LocateResult> = _locateResult

    fun tryLocate() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                locationHelper.tryLocate()
            }
            _locateResult.value = result
        }
    }
}