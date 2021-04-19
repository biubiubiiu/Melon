package app.melon.poi

import android.content.Context
import app.melon.poi.api.IPoiService
import app.melon.poi.api.PoiInfo

class PoiService : IPoiService {
    override fun navigateToPoiDetail(context: Context, info: PoiInfo) {
        PoiDetailActivity.start(context, info)
    }
}