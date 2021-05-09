package app.melon.composer.selectlocation

import androidx.annotation.WorkerThread
import app.melon.data.entities.PoiInfo
import app.melon.location.LocateFail
import app.melon.location.LocateSuccess
import app.melon.location.LocationHelper
import app.melon.location.PoiSearchHelper
import app.melon.util.extensions.toException
import javax.inject.Inject

internal class SearchLocationRepository @Inject constructor(
    private val locationHelper: LocationHelper,
    private val poiSearchHelper: PoiSearchHelper
) {

    @WorkerThread
    suspend fun getSearchResult(query: String): Result<List<PoiInfo>> {
        val searchResult = when (val locateResult = locationHelper.tryLocate()) {
            is LocateFail -> return Result.failure(locateResult.errorMessage.toException())
            is LocateSuccess -> poiSearchHelper.searchByKeyword(
                locateResult.longitude,
                locateResult.latitude,
                query
            )
        }
        return searchResult.fold(
            onSuccess = {
                Result.success(searchResult.getOrNull()!!.map {
                    PoiInfo(
                        it.poiId,
                        it.title,
                        it.latLonPoint.longitude,
                        it.latLonPoint.latitude
                    )
                })
            },
            onFailure = { Result.failure(it) }
        )
    }
}