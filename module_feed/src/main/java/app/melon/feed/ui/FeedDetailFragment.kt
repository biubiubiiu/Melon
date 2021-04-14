package app.melon.feed.ui

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.melon.base.databinding.FragmentEpoxyListBinding
import app.melon.base.framework.BaseMvRxEpoxyFragment
import app.melon.data.resultentities.FeedAndAuthor
import app.melon.feed.ui.controller.FeedDetailController
import javax.inject.Inject


class FeedDetailFragment : BaseMvRxEpoxyFragment() {

    private val cache by lazy { requireNotNull(arguments?.get(KEY_FEED_CACHE) as? FeedAndAuthor) }

    @Inject internal lateinit var viewModel: FeedDetailViewModel

    @Inject internal lateinit var controllerFactory: FeedDetailController.Factory

    override val controller by lazy {
        controllerFactory.create(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.refresh(cache)
    }

    override fun onViewCreated(binding: FragmentEpoxyListBinding, savedInstanceState: Bundle?) {
        super.onViewCreated(binding, savedInstanceState)
        binding.swipeRefreshLayout.isEnabled = false // disable swipe refresh
        binding.recyclerView.setController(controller)
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy <= 0) {
                    return
                }
                val viewManager = recyclerView.layoutManager as? LinearLayoutManager ?: return
                val visibleItemCount = viewManager.childCount
                val totalItemCount = viewManager.itemCount
                val lastVisibleItem = viewManager.findLastVisibleItemPosition()

                if (visibleItemCount + lastVisibleItem + 3 >= totalItemCount) {
                    viewModel.loadMoreComment()
                }
            }
        })

        viewModel.liveData.observe(viewLifecycleOwner, Observer {
            controller.setData(it.pageItem, it.comments, it.refreshing)
        })
    }

    override fun invalidate() {
        // No-op
    }

    companion object {
        private const val KEY_FEED_CACHE = "KEY_FEED_CACHE"

        fun newInstance(cache: FeedAndAuthor): FeedDetailFragment {
            return FeedDetailFragment().apply {
                arguments = bundleOf(KEY_FEED_CACHE to cache)
            }
        }
    }
}