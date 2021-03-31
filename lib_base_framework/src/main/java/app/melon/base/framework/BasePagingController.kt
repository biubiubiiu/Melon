package app.melon.base.framework

import android.content.Context
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import app.melon.base.ui.R
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
import java.net.SocketTimeoutException


@OptIn(ObsoleteCoroutinesApi::class)
abstract class BasePagingController<T : Any>(
    protected val context: Context
) : PagingDataEpoxyController<T>(
    modelBuildingHandler = EpoxyAsyncUtil.getAsyncBackgroundHandler(),
    diffingHandler = EpoxyAsyncUtil.getAsyncBackgroundHandler()
) {

    protected open val loadMoreView = LoadMoreView_().apply { id(LoadMoreView::class.java.simpleName) }
    protected open val refreshingView = RefreshView_().apply { id(RefreshView::class.java.simpleName) }
    protected open val emptyView = EmptyView_().apply { id(EmptyView::class.java.simpleName) }
    protected open val errorView = ErrorView_().apply {
        id(ErrorView::class.java.simpleName)
        retryListener { retry() }
    }

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
    val isRefreshIdle get() = loadState?.refresh is LoadState.NotLoading
    val isRefreshError get() = loadState?.refresh is LoadState.Error
    val isLoadingMore get() = loadState?.append is LoadState.Loading

    override fun addModels(models: List<EpoxyModel<*>>) {
        addExtraModels()
        super.addModels(models)
        loadMoreView.addIf(isLoadingMore && models.isNotEmpty(), this)
        refreshingView.addIf(isRefreshing && models.isEmpty(), this)
        emptyView.addIf(isRefreshIdle && models.isEmpty(), this)
        if (isRefreshError && models.isEmpty()) {
            addErrorView()
        }
    }

    /**
     *
     * - if timeout, it throws [java.net.SocketTimeoutException]
     */
    open fun addErrorView() {
        val error = (loadState?.refresh as? LoadState.Error)?.error ?: return
        val subtitle = when (error) {
            is SocketTimeoutException -> R.string.app_common_error_timeout_hint
            else -> R.string.app_common_error_unknown_hint
        }
        errorView.apply {
            subtitleRes(subtitle)
        }.addTo(this)
    }

    open fun addExtraModels() {}
}