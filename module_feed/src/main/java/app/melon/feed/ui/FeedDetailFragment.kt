package app.melon.feed.ui

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import app.melon.base.databinding.FragmentEpoxyListBinding
import app.melon.base.framework.BaseEpoxyListFragment
import app.melon.data.resultentities.FeedAndAuthor
import app.melon.feed.R
import app.melon.feed.ui.controller.FeedDetailController
import app.melon.feed.ui.state.FeedDetailViewState
import app.melon.util.extensions.showSnackbarLong
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


class FeedDetailFragment : BaseEpoxyListFragment() {

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

        lifecycleScope.launch {
            viewModel.commentsPagingData.collectLatest {
                controller.submitData(it)
            }
        }
        viewModel.selectObserve(FeedDetailViewState::pageItem).observe(viewLifecycleOwner, Observer {
            controller.item = it
        })
        viewModel.selectObserve(FeedDetailViewState::error).observe(viewLifecycleOwner, Observer {
            it ?: return@Observer
            binding.root.showSnackbarLong(it.message ?: getString(R.string.app_common_error_unknown_hint))
        })
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