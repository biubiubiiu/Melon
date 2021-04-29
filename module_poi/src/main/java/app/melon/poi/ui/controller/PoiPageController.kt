package app.melon.poi.ui.controller

import android.content.Context
import app.melon.base.framework.BasePagingController
import app.melon.base.ui.list.divider
import app.melon.base.ui.list.vertSpaceConventional
import app.melon.base.ui.textHeader
import app.melon.data.resultentities.FeedAndAuthor
import app.melon.feed.ui.controller.FeedControllerDelegate
import app.melon.location.SimplifiedLocation
import app.melon.poi.R
import app.melon.poi.data.PoiStruct
import app.melon.poi.ui.widget.locationInfo
import app.melon.util.extensions.dpInt
import app.melon.util.extensions.getResourceColor
import app.melon.util.extensions.sp
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.group
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


internal class PoiPageController @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted private val shareRoute: (SimplifiedLocation) -> Unit,
    factory: FeedControllerDelegate.Factory
) : BasePagingController<FeedAndAuthor>(
    context,
    sameItemIndicator = { oldItem, newItem -> oldItem.feed.id == newItem.feed.id }
) {

    internal var poiData: PoiStruct? = null
        set(value) {
            field = value
            requestModelBuild()
        }

    private val delegate = factory.create(context)

    override fun buildItemModel(currentPosition: Int, item: FeedAndAuthor?): EpoxyModel<*> {
        return delegate.buildFeedItem(
            dataProvider = { item!! },
            idProvider = { "poi_feeds_$currentPosition" }
        )
    }

    override fun addExtraModels() {
        group {
            id("poi_location_info_header")
            layout(R.layout.layout_vertical_linear_group)
            locationInfo {
                id("poi_location_info")
                item(poiData)
                navigationListener {
                    shareRoute(it)
                }
            }
            vertSpaceConventional {
                id("info_bottom_spacer")
                bgColor(R.color.bgPrimary)
            }
            divider {
                id("info_bottom_divider")
            }
        }
        textHeader {
            id("all_posts_header")
            background(R.color.bgPrimary)
            color(getResourceColor(R.color.TextPrimary))
            textSize(15.sp)
            content(R.string.poi_all_posts)
            padding(intArrayOf(12.dpInt, 16.dpInt))
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            context: Context,
            shareRoute: (SimplifiedLocation) -> Unit
        ): PoiPageController
    }
}