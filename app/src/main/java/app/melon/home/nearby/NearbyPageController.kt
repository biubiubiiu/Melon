package app.melon.home.nearby

import android.content.Context
import app.melon.base.framework.BasePagingController
import app.melon.data.entities.User
import com.airbnb.epoxy.EpoxyModel

class NearbyPageController(
    context: Context
) : BasePagingController<User>(context) {
    override fun buildItemModel(currentPosition: Int, item: User?): EpoxyModel<*> {
        return NearbyUserItem_()
            .id("nearby_user_$currentPosition")
            .user(item!!)
    }
}