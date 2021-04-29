package app.melon.poi.ui

import app.melon.base.framework.SingleEvent
import app.melon.location.SimplifiedLocation
import app.melon.poi.data.PoiStruct
import com.amap.api.services.route.DrivePath
import com.amap.api.services.route.WalkPath

internal data class PoiDetailViewState(
    val poiId: String,
    val location: OriginDestinationLocation? = null,
    val poiData: PoiStruct? = null,
    val walkPath: WalkPath? = null,
    val drivePath: DrivePath? = null,
    val shareRoute: SingleEvent<String?>? = null,
    val refreshing: Boolean = false,
    val error: Throwable? = null
)

internal data class OriginDestinationLocation(
    val origin: SimplifiedLocation? = null,
    val destination: SimplifiedLocation? = null
)