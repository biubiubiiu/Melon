package app.melon.home.recommend.ui

import android.content.Context
import app.melon.base.R
import app.melon.base.framework.BaseFeedPagingController
import app.melon.base.ui.list.vertSpaceMicro
import app.melon.base.ui.list.vertSpaceSmall
import app.melon.base.uikit.list.carouselHeader
import app.melon.data.entities.InterestGroup
import app.melon.data.resultentities.RecommendedEntryWithFeed
import app.melon.extensions.observable
import app.melon.home.recommend.groups.GroupCarouselController4Test
import app.melon.home.recommend.groups.GroupItem_
import app.melon.home.recommend.groups.groupCarousel
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.group


class RecommendPageController(
    context: Context
) : BaseFeedPagingController<RecommendedEntryWithFeed>(context) {

    var callbacks: Actions? by observable(null, ::requestModelBuild)

    override fun addExtraModels() {
        val carouselController = GroupCarouselController4Test()
        val testData = (0L..10L).map {
            InterestGroup(it, "", "", 0, "")
        }
        val modelsInCarousel = testData.map {
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


    override fun buildItemModel(currentPosition: Int, item: RecommendedEntryWithFeed?): EpoxyModel<*> =
        RecommendFeedItem_()
            .id("feed ${item!!.entry.id}")
            .item(item.feed)
            .holderClickListener { callbacks?.onHolderClick() }
            .avatarClickListener { callbacks?.onAvatarClick() }
            .shareClickListener { callbacks?.onShareClick() }
            .commentClickListener { callbacks?.onCommentClick() }
            .favorClickListener { callbacks?.onFavorClick() }
            .moreClickListener { callbacks?.onMoreClick() }

    fun clear() {
        callbacks = null
    }

    interface Actions {
        fun onHolderClick()
        fun onAvatarClick()
        fun onShareClick()
        fun onCommentClick()
        fun onFavorClick()
        fun onMoreClick()
    }
}