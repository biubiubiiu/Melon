package app.melon.home.recommend.ui

import android.content.Context
import app.melon.base.R
import app.melon.base.framework.BasePagingController
import app.melon.base.ui.list.vertSpaceMicro
import app.melon.base.ui.list.vertSpaceSmall
import app.melon.base.uikit.list.carouselHeader
import app.melon.data.entities.InterestGroup
import app.melon.data.resultentities.RecommendedEntryWithFeed
import app.melon.feed.FeedControllerDelegate
import app.melon.home.recommend.groups.GroupCarouselController4Test
import app.melon.home.recommend.groups.GroupItem_
import app.melon.home.recommend.groups.groupCarousel
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.group
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


class RecommendPageController @AssistedInject constructor(
    @Assisted context: Context,
    private val factory: FeedControllerDelegate.Factory
) : BasePagingController<RecommendedEntryWithFeed>(context) {

    private val delegate by lazy {
        factory.create(context)
    }

    override fun buildItemModel(currentPosition: Int, item: RecommendedEntryWithFeed?): EpoxyModel<*> =
        delegate.buildFeedItem(
            feedProvider = { item!!.feed },
            idProvider = { "recommend_feed${item!!.entry.feedId}" }
        )

    override fun addExtraModels() {
        val carouselController = GroupCarouselController4Test()
        val modelsInCarousel = (0L..10L).map {
            InterestGroup(it, "", "", 0, "")
        }.map {
            GroupItem_()
                .id("interest group ${it.id}")
                .showPicUrl("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3478126314,1020520393&fm=26&gp=0.jpg")
                .groupName("test")
                .clickListener {}
        }
        group {
            id("interest group carousel with header")
            layout(R.layout.layout_vertical_linear_group)
            vertSpaceMicro {
                id("recommend header spacer")
            }
            carouselHeader {
                id("interest group header")
                title("My groups")
                trailingText("More")
            }
            groupCarousel {
                id("interest group list")
                models(modelsInCarousel) // TODO replace with emptyList
                epoxyController(carouselController)
                paddingDp(0)
                hasFixedSize(true)
                numViewsToShowOnScreen(4f)
            }
            vertSpaceSmall {
                id("group carousel bottom spacer")
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(context: Context): RecommendPageController
    }
}
