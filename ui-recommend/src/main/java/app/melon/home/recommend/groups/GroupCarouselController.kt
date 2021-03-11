package app.melon.home.recommend.groups

import app.melon.base.framework.BaseCarouselController
import app.melon.data.entities.InterestGroup
import com.airbnb.epoxy.EpoxyModel

class GroupCarouselController(
    override val listener: (InterestGroup) -> Unit
) : BaseCarouselController<InterestGroup>(listener) {
    override fun buildItemModel(currentPosition: Int, item: InterestGroup?): EpoxyModel<*> {
        return GroupItem_()
            .id("interest group ${item!!.id}")
            .showPicUrl("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3478126314,1020520393&fm=26&gp=0.jpg")
            .groupName("test")
            .clickListener(listener)
    }
}