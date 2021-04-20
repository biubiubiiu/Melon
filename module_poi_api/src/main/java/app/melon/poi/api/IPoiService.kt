package app.melon.poi.api

import android.content.Context
import app.melon.data.entities.PoiInfo

interface IPoiService {
    fun navigateToPoiDetail(context: Context, info: PoiInfo)
}