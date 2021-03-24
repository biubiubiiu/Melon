package app.melon.base.framework

import android.content.Context
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import app.melon.base.R
import app.melon.base.ui.list.EmptyView
import app.melon.base.ui.list.EmptyView_
import app.melon.base.ui.list.ErrorView
import app.melon.base.ui.list.ErrorView_
import app.melon.base.ui.list.LoadMoreView
import app.melon.base.ui.list.LoadMoreView_
import app.melon.base.ui.list.RefreshView
import app.melon.base.ui.list.RefreshView_
import com.airbnb.epoxy.EpoxyAsyncUtil
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging3.PagingDataEpoxyController
import kotlinx.coroutines.ObsoleteCoroutinesApi


@OptIn(ObsoleteCoroutinesApi::class)
abstract class BasePagingController<T : Any>(
    protected val context: Context
) : PagingDataEpoxyController<T>(
    modelBuildingHandler = EpoxyAsyncUtil.getAsyncBackgroundHandler(),
    diffingHandler = EpoxyAsyncUtil.getAsyncBackgroundHandler()
) {

    init {
        addLoadStateListener {
            loadState = it
        }
    }

    protected var loadState: CombinedLoadStates? = null
        set(value) {
            field = value
            requestModelBuild()
        }

    val isRefreshing get() = loadState?.refresh is LoadState.Loading
    val isLoadingMore get() = loadState?.append is LoadState.Loading
    val isRefreshError get() = loadState?.refresh is LoadState.Error

    protected open var loadMoreView: EpoxyModel<*>? = LoadMoreView_().apply { id(LoadMoreView::class.java.simpleName) }
    protected open var refreshingView: EpoxyModel<*>? = RefreshView_().apply { id(RefreshView::class.java.simpleName) }
    protected open var errorView: EpoxyModel<*>? = ErrorView_().apply { id(ErrorView::class.java.simpleName) }
    protected open var emptyView: EpoxyModel<*>? = EmptyView_().apply {
        id(EmptyView::class.java.simpleName)
        title(context.getString(R.string.empty_list_title))
        subtitle(context.getString(R.string.empty_list_subtitile))
    }

    override fun addModels(models: List<EpoxyModel<*>>) {
        addExtraModels()
        super.addModels(models)
        loadMoreView?.addIf(isLoadingMore && models.isNotEmpty(), this)
        refreshingView?.addIf(isRefreshing && models.isEmpty(), this)
        emptyView?.addIf(!isRefreshing && models.isEmpty(), this)
        errorView?.addIf(isRefreshError && models.isEmpty(), this)
        // if timeout, it throws [java.net.SocketTimeoutException]
    }

    open fun addExtraModels() {}
}