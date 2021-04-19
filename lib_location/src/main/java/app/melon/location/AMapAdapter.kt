package app.melon.location

import com.amap.api.maps2d.model.LatLng
import com.amap.api.services.core.LatLonPoint

fun SimplifiedLocation.toLatLng(): LatLng {
    return LatLng(
        this.latitude,
        this.longitude
    )
}

fun SimplifiedLocation.toLatLonPoint(): LatLonPoint {
    return LatLonPoint(
        this.latitude,
        this.longitude
    )
}

fun LatLonPoint.toSimplifiedLocation(): SimplifiedLocation {
    return SimplifiedLocation(
        this.longitude,
        this.latitude
    )
}