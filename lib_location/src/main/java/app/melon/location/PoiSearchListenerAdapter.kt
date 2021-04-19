package app.melon.location

import com.amap.api.services.core.PoiItem
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch

/**
 * This adapter class provides empty implementations of the methods from [PoiSearch.OnPoiSearchListener]
 * Any custom listener that cares only about a subset of the methods of this listener can
 * simply subclass this adapter class instead of implementing the interface directly.
 */
abstract class PoiSearchListenerAdapter : PoiSearch.OnPoiSearchListener {

    override fun onPoiSearched(result: PoiResult?, rCode: Int) {}

    override fun onPoiItemSearched(item: PoiItem?, rCode: Int) {}
}