package app.melon.composer.selectlocation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.melon.data.entities.PoiInfo
import app.melon.util.base.ErrorResult
import app.melon.util.base.Success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class SelectLocationViewModel @Inject constructor(
    private val repo: SearchLocationRepository
) : ViewModel() {

    private var currentQueryValue: String? = null
    private var currentQueryResult: List<PoiInfo>? = null

    private val _refreshing = MutableLiveData<Boolean>()
    val refreshing: LiveData<Boolean> = _refreshing

    private val _searchError = MutableLiveData<String>()
    val searchError: LiveData<String> = _searchError


    fun searchLocation(query: String): Flow<List<PoiInfo>> = flow {
        _refreshing.value = true

        val lastResult = currentQueryResult
        if (query == currentQueryValue) {
            lastResult?.let {
                emit(it)
                _refreshing.value = false
            }
        }
        currentQueryValue = query
        val searchResult = withContext(Dispatchers.IO) {
            repo.getSearchResult(query)
        }
        when (searchResult) {
            is Success -> currentQueryResult = searchResult.data.also { emit(it) }
            is ErrorResult -> _searchError.value = searchResult.throwable.message
        }
        _refreshing.value = false
    }
}