package app.melon.home.recommend.groups

import app.melon.data.entities.InterestGroup
import com.airbnb.epoxy.Typed2EpoxyController

class GroupCarouselController4Test() : Typed2EpoxyController<List<InterestGroup>, Boolean>() {

    override fun buildModels(groups: List<InterestGroup>, loadingMore: Boolean) {
        groups.forEach {
            groupItem {
                id("interest group ${it.id}")
                showPicUrl("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3478126314,1020520393&fm=26&gp=0.jpg")
                groupName("test")
                clickListener {}
            }
        }
    }
}