package app.melon.location

import com.amap.api.services.share.ShareSearch

/**
 * This adapter class provides empty implementations of the methods from [ShareSearch.OnShareSearchListener]
 * Any custom listener that cares only about a subset of the methods of this listener can
 * simply subclass this adapter class instead of implementing the interface directly.
 */
abstract class ShareSearchListenerAdapter : ShareSearch.OnShareSearchListener {

    override fun onPoiShareUrlSearched(url: String?, errorCode: Int) {}

    override fun onLocationShareUrlSearched(url: String?, errorCode: Int) {}

    override fun onNaviShareUrlSearched(url: String?, errorCode: Int) {}

    override fun onBusRouteShareUrlSearched(url: String?, errorCode: Int) {}

    override fun onWalkRouteShareUrlSearched(url: String?, errorCode: Int) {}

    override fun onDrivingRouteShareUrlSearched(url: String?, errorCode: Int) {}
}