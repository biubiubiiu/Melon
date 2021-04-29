package app.melon.poi

import android.content.Context
import app.melon.data.entities.PoiInfo
import app.melon.poi.api.IPoiService

internal class PoiService : IPoiService {
    override fun navigateToPoiDetail(context: Context, info: PoiInfo) {
        PoiDetailActivity.start(context, info)
    }
}