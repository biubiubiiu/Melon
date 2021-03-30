package app.melon.group

import app.melon.data.entities.InterestGroup
import com.airbnb.epoxy.Typed2EpoxyController

class GroupCarouselController(
    private val enterDetailListener: (InterestGroup) -> Unit
) : Typed2EpoxyController<List<InterestGroup>, Int>() {

    override fun buildModels(groups: List<InterestGroup>, showItems: Int) {
        groups.take(showItems).forEach {
            groupItem {
                id("interest group ${it.id}")
                showPicUrl("https://ss3.bdstatGic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3478126314,1020520393&fm=26&gp=0.jpg")
                groupName("test")
                clickListener(enterDetailListener)
            }
        }
        if (groups.size > showItems) {
            groupItem {
                id("interest group show more")
                showPicId(R.drawable.ic_baseline_read_more_24)
                groupName("View More")
                clickListener { }
            }
        }
    }
}