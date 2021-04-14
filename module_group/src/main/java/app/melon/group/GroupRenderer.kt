package app.melon.group

import android.content.Context
import app.melon.base.ui.carouselHeader
import app.melon.base.ui.list.vertSpaceMicro
import app.melon.base.ui.list.vertSpaceSmall
import app.melon.data.entities.Group
import app.melon.group.data.GroupRepository
import app.melon.util.extensions.showToast
import com.airbnb.epoxy.ModelCollector
import com.airbnb.epoxy.group
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class GroupRenderer @AssistedInject constructor(
    @Assisted private val context: Context, // TODO we need a token manager here so it can request data on its own
    @Assisted private val scope: CoroutineScope,
    private val repository: GroupRepository
) : GroupActions {

    private val carouselController by lazy {
        GroupCarouselController(::onHolderClick)
    }

    fun addGroupCarousel(collector: ModelCollector) {
        with(collector) {
            group {
                id("interest group carousel with header")
                layout(R.layout.layout_vertical_linear_group)
                vertSpaceMicro {
                    id("interest group header spacer")
                }
                carouselHeader {
                    id("interest group header")
                    title("My groups")
                    trailingText("More")
                }
                groupCarousel {
                    id("interest group list")
                    models(emptyList()) // TODO replace with emptyList
                    epoxyController(carouselController)
                    paddingDp(0)
                    hasFixedSize(true)
                    numViewsToShowOnScreen(4.5f)
                }
                vertSpaceSmall {
                    id("group carousel bottom spacer")
                }
            }
        }
        scope.launch {
            val items = repository.getFirstPageGroups()
            carouselController.setData(items, SHOWED_GROUPS)
        }
    }

    override fun onHolderClick(group: Group) {
        context.showToast("click group holder")
    }

    @AssistedFactory
    interface Factory {
        fun create(context: Context, scope: CoroutineScope): GroupRenderer
    }

    companion object {
        private const val SHOWED_GROUPS = 8
    }
}