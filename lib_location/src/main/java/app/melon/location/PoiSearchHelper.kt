package app.melon.location

import android.content.Context
import app.melon.util.extensions.toException
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.PoiItem
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


@Singleton
class PoiSearchHelper @Inject constructor(
    private val context: Context
) {

    suspend fun searchByKeyword(
        longitude: Double,
        latitude: Double,
        keyword: String,
        queryRadius: Int = 1000,
        pageSize: Int = 20,
        pageNum: Int = 0
    ) = searchByKeyword(
        SimplifiedLocation(longitude, latitude),
        keyword,
        queryRadius,
        pageSize,
        pageNum
    )

    suspend fun searchByKeyword(
        location: SimplifiedLocation,
        keyword: String,
        queryRadius: Int = 1000,
        pageSize: Int = 20,
        pageNum: Int = 0
    ): Result<List<PoiItem>> {
        return suspendCoroutine {
            val query = PoiSearch.Query(keyword, "", "")
            query.pageSize = pageSize
            query.pageNum = pageNum

            val poiSearch = PoiSearch(context, query)
            poiSearch.bound = PoiSearch.SearchBound(location.toLatLonPoint(), queryRadius)
            poiSearch.setOnPoiSearchListener(object : PoiSearchListenerAdapter() {
                override fun onPoiSearched(result: PoiResult?, rCode: Int) {
                    if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                        it.resume(Result.success(result?.pois?.toList() ?: emptyList()))
                    } else {
                        it.resume(Result.failure("Poi Search failed with error code: $rCode".toException()))
                    }
                }
            })
            poiSearch.searchPOIAsyn()
        }
    }

    suspend fun searchByPoiId(id: String): Result<PoiItem?> {
        return suspendCoroutine {
            val poiSearch = PoiSearch(context, null)
            poiSearch.setOnPoiSearchListener(object : PoiSearchListenerAdapter() {
                override fun onPoiItemSearched(item: PoiItem?, rCode: Int) {
                    if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                        it.resume(Result.success(item))
                    } else {
                        it.resume(Result.failure("Poi Search failed with error code: $rCode".toException()))
                    }
                }
            })
            poiSearch.searchPOIIdAsyn(id)
        }
    }
}