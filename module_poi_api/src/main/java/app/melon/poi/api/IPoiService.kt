package app.melon.poi.api

import android.content.Context

interface IPoiService {
    fun navigateToPoiDetail(context: Context, info: PoiInfo)
}