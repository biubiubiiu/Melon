package app.melon.composer.selectlocation

import android.util.Log
import androidx.annotation.WorkerThread
import app.melon.data.entities.PoiInfo
import app.melon.location.LocateFail
import app.melon.location.LocateSuccess
import app.melon.location.LocationHelper
import app.melon.location.PoiSearchHelper
import app.melon.util.base.ErrorResult
import app.melon.util.base.Result
import app.melon.util.base.Success
import app.melon.util.extensions.toException
import javax.inject.Inject

internal class SearchLocationRepository @Inject constructor(
    private val locationHelper: LocationHelper,
    private val poiSearchHelper: PoiSearchHelper
) {

    @WorkerThread
    suspend fun getSearchResult(query: String): Result<List<PoiInfo>> {
        val locateResult = locationHelper.tryLocate()
        val searchResult = when (locateResult) {
            is LocateFail -> return ErrorResult(locateResult.errorMessage.toException())
            is LocateSuccess -> poiSearchHelper.searchByKeyword(
                locateResult.longitude,
                locateResult.latitude,
                query
            )
        }
        Log.d("raymond", "locate result: $locateResult")
        Log.d("raymond", "search result: $searchResult")
        return when (searchResult) {
            is Success -> Success(searchResult.data.map {
                PoiInfo(
                    it.poiId,
                    it.title,
                    it.latLonPoint.longitude,
                    it.latLonPoint.latitude
                )
            })
            is ErrorResult -> ErrorResult(searchResult.throwable)
        }
    }
}