package app.melon.poi.ui

import app.melon.base.framework.SingleEvent
import app.melon.poi.data.PoiStruct
import com.amap.api.services.route.DrivePath
import com.amap.api.services.route.WalkPath

data class PoiDetailViewState(
    val poiData: PoiStruct? = null,
    val walkPath: WalkPath? = null,
    val drivePath: DrivePath? = null,
    val shareRoute: SingleEvent<String?>? = null,
    val error: Throwable? = null
)