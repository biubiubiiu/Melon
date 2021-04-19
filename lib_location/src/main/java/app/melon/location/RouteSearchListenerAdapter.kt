package app.melon.location

import com.amap.api.services.route.BusRouteResult
import com.amap.api.services.route.DriveRouteResult
import com.amap.api.services.route.RideRouteResult
import com.amap.api.services.route.RouteSearch
import com.amap.api.services.route.WalkRouteResult


/**
 * This adapter class provides empty implementations of the methods from [RouteSearch.OnRouteSearchListener]
 * Any custom listener that cares only about a subset of the methods of this listener can
 * simply subclass this adapter class instead of implementing the interface directly.
 */
abstract class RouteSearchListenerAdapter : RouteSearch.OnRouteSearchListener {

    override fun onBusRouteSearched(result: BusRouteResult?, errorCode: Int) {}

    override fun onDriveRouteSearched(result: DriveRouteResult?, errorCode: Int) {}

    override fun onWalkRouteSearched(result: WalkRouteResult?, errorCode: Int) {}

    override fun onRideRouteSearched(result: RideRouteResult?, errorCode: Int) {}
}