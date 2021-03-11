package app.melon.base.framework

import android.annotation.SuppressLint
import android.content.Context
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.DiffUtil
import app.melon.base.R
import app.melon.base.uikit.list.EmptyView
import app.melon.base.uikit.list.EmptyView_
import app.melon.base.uikit.list.ErrorView
import app.melon.base.uikit.list.ErrorView_
import app.melon.base.uikit.list.LoadMoreView
import app.melon.base.uikit.list.LoadMoreView_
import app.melon.base.uikit.list.RefreshView
import app.melon.base.uikit.list.RefreshView_
import app.melon.data.resultentities.EntryWithFeed
import com.airbnb.epoxy.EpoxyAsyncUtil
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging3.PagingDataEpoxyController
import kotlinx.coroutines.ObsoleteCoroutinesApi


@OptIn(ObsoleteCoroutinesApi::class)
abstract class BasePagingController<T : EntryWithFeed<*>>(
    protected val context: Context
) : PagingDataEpoxyController<T>(
    modelBuildingHandler = EpoxyAsyncUtil.getAsyncBackgroundHandler(),
    diffingHandler = EpoxyAsyncUtil.getAsyncBackgroundHandler(),
    itemDiffCallback = object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = oldItem.entry.feedId == newItem.entry.feedId

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
    }
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
        loadMoreView?.addIf(loadState?.append is LoadState.Loading && models.isNotEmpty(), this)
        refreshingView?.addIf(loadState?.refresh is LoadState.Loading, this)
        emptyView?.addIf(loadState?.refresh is LoadState.NotLoading && models.isEmpty(), this)
        errorView?.addIf(loadState?.refresh is LoadState.Error, this)
        (loadState?.refresh as? LoadState.Error)?.run {
            throw error
        }
        // if timeout, it throws [java.net.SocketTimeoutException]
    }

    open fun addExtraModels() {}
}