package app.melon.location

import android.content.Context
import app.melon.util.base.ErrorResult
import app.melon.util.base.Result
import app.melon.util.base.Success
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

    suspend fun searchByKeyword(keyword: String) {
        return suspendCoroutine {
            val query = PoiSearch.Query(keyword, "", "")
            query.pageSize = 10
            query.pageNum = 0

            val poiSearch = PoiSearch(context, query)
            poiSearch.setOnPoiSearchListener(object : PoiSearchListenerAdapter() {
                override fun onPoiSearched(result: PoiResult?, rCode: Int) {
                    it.resume(Unit)
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
                        it.resume(Success(item))
                    } else {
                        it.resume(ErrorResult("Poi Search failed with error code: $rCode".toException()))
                    }
                }
            })
            poiSearch.searchPOIIdAsyn(id)
        }
    }
}