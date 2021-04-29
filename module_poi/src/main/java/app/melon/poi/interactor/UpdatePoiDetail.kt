package app.melon.poi.interactor

import app.melon.base.domain.SuspendingWorkInteractor
import app.melon.location.PoiSearchHelper
import app.melon.location.toSimplifiedLocation
import app.melon.poi.data.PoiStruct
import app.melon.util.base.ErrorResult
import app.melon.util.base.Result
import app.melon.util.base.Success
import com.amap.api.services.core.PoiItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


internal class UpdatePoiDetail @Inject constructor(
    private val poiSearchHelper: PoiSearchHelper
) : SuspendingWorkInteractor<UpdatePoiDetail.Params, Result<PoiStruct?>>() {

    override suspend fun doWork(params: Params): Result<PoiStruct?> {
        val result = withContext(Dispatchers.IO) {
            poiSearchHelper.searchByPoiId(params.id)
        }
        return when (result) {
            is Success -> Success(result.get().toPoiStruct())
            is ErrorResult -> ErrorResult(result.throwable)
        }
    }

    private fun PoiItem?.toPoiStruct(): PoiStruct? {
        if (this == null) {
            return null
        }
        return PoiStruct(
            name = this.title,
            district = this.adName,
            type = this.typeDes,
            address = this.snippet,
            location = this.latLonPoint.toSimplifiedLocation()
        )
    }

    internal data class Params(
        val id: String
    )
}