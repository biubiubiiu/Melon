package app.melon.poi.data

import app.melon.data.entities.PoiInfo
import app.melon.location.SimplifiedLocation

internal val PoiInfo.location: SimplifiedLocation
    get() = SimplifiedLocation(
        this.longitude,
        this.latitude
    )