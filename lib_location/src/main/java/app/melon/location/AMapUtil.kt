package app.melon.location

import com.amap.api.maps2d.model.LatLng
import com.amap.api.services.core.LatLonPoint

fun LatLonPoint.toLatLng() = LatLng(this.latitude, this.longitude)