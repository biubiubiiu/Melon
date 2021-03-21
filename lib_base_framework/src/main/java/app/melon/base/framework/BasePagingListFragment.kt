package app.melon.base.framework

import android.os.Bundle
import androidx.paging.LoadState
import app.melon.base.databinding.FragmentEpoxyListBinding
import app.melon.data.resultentities.EntryWithFeed

abstract class BasePagingListFragment : BaseMvRxEpoxyFragment() {

    abstract override val controller: BasePagingController<out EntryWithFeed<*>>

    override fun onViewCreated(binding: FragmentEpoxyListBinding, savedInstanceState: Bundle?) {
        super.onViewCreated(binding, savedInstanceState)
        controller.addLoadStateListener {
            binding.swipeRefreshLayout.isRefreshing = it.refresh is LoadState.Loading
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            controller.takeIf { !it.isRefreshing }?.refresh()
        }
    }
}