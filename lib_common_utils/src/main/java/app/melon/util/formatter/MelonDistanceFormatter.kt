package app.melon.util.formatter

import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MelonDistanceFormatter @Inject constructor(){

    fun formatDistance(distance: Float?): String {
        distance ?: return ""
        if (distance > 1000f) {
            return "%.1fkm".format(distance / 1000)
        }
        return "%.0fm".format(distance)
    }
}