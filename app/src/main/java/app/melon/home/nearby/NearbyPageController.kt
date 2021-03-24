package app.melon.home.nearby

import android.content.Context
import app.melon.base.framework.BasePagingController
import app.melon.data.entities.User
import app.melon.extensions.observable
import com.airbnb.epoxy.EpoxyModel

class NearbyPageController(
    context: Context
) : BasePagingController<User>(context) {

    var callbacks: Actions? by observable(null, ::requestModelBuild)

    override fun buildItemModel(currentPosition: Int, item: User?): EpoxyModel<*> {
        return NearbyUserItem_()
            .id("nearby_user_$currentPosition")
            .holderClickListener { callbacks?.onHolderClick(item) }
            .user(item!!)
    }

    fun clear() {
        callbacks = null
    }

    interface Actions {
        fun onHolderClick(user: User?)
    }
}