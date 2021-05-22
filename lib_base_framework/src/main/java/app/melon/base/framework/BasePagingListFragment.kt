package app.melon.base.framework

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.paging.LoadState
import app.melon.base.databinding.FragmentEpoxyListBinding

abstract class BasePagingListFragment : BaseEpoxyListFragment() {

    abstract override val controller: BasePagingController<*>

    @CallSuper
    override fun onViewCreated(binding: FragmentEpoxyListBinding, savedInstanceState: Bundle?) {
        super.onViewCreated(binding, savedInstanceState)
        controller.addLoadStateListener {
            binding.swipeRefreshLayout.post {
                binding.swipeRefreshLayout.isRefreshing = it.refresh !is LoadState.NotLoading
            }
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            controller.refresh()
        }
    }
}