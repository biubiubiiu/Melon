package app.melon.base.framework

import androidx.paging.LoadState
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging3.PagingDataEpoxyController
import kotlinx.coroutines.ObsoleteCoroutinesApi

@OptIn(ObsoleteCoroutinesApi::class)
abstract class BaseCarouselController<T : Any>(
    protected open val listener: (T) -> Unit,
    protected open val loadMoreView: EpoxyModel<*>? = null
) : PagingDataEpoxyController<T>() {

    protected var loadingMore = false
        set(value) {
            field = value
            requestModelBuild()
        }

    protected var errorOccur = false
        set(value) {
            field = value
            requestModelBuild()
        }

    /**
     * Whether initial load or refresh was failed
     */
    protected var refreshError = false
        set(value) {
            field = value
            requestModelBuild()
        }

    init {
        addLoadStateListener {
            loadingMore = it.source.append is LoadState.Loading
            refreshError = it.source.refresh is LoadState.Error
            errorOccur = it.source.append as? LoadState.Error
                    ?: it.source.prepend as? LoadState.Error
                    ?: it.append as? LoadState.Error
                    ?: it.prepend as? LoadState.Error != null
        }
    }

    override fun addModels(models: List<EpoxyModel<*>>) {
        super.addModels(models)
        loadMoreView?.addIf(loadingMore && models.isNotEmpty(), this)
    }
}