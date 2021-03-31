package app.melon.feed.anonymous.ui

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.paging.flatMap
import androidx.paging.map
import app.melon.base.databinding.FragmentEpoxyListBinding
import app.melon.base.framework.BasePagingListFragment
import app.melon.feed.anonymous.AnonymousFeedViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class SchoolFeedFragment : BasePagingListFragment() {

    @Inject internal lateinit var viewModel: AnonymousFeedViewModel
    @Inject internal lateinit var controllerFactory: FeedPageController.Factory

    override val controller by lazy {
        controllerFactory.create(
            context = requireContext(),
            idProvider = { feed, _ -> "anonymous_school_feeds_${feed!!.feedId}" },
            type = FeedPageController.Type.ANONYMOUS
        )
    }

    private var fetchJob: Job? = null

    private fun refresh() {
        fetchJob?.cancel()
        fetchJob = lifecycleScope.launch {
            viewModel.refreshSchoolFeeds(timestamp = 1000000).collectLatest {
                controller.submitData(it.map { entryWithFeed -> entryWithFeed.feed })
            }
        }
    }

    private val pageIndex by lazy(LazyThreadSafetyMode.NONE) { arguments?.getInt(FIELD_PAGE, -1) }

    override fun onViewCreated(binding: FragmentEpoxyListBinding, savedInstanceState: Bundle?) {
        super.onViewCreated(binding, savedInstanceState)
        binding.recyclerView.setItemSpacingDp(8)
        refresh()
    }

    override fun invalidate() {
        // no-op
    }

    companion object {
        private const val FIELD_PAGE = "page"

        fun newInstance(page: Int) = SchoolFeedFragment().apply {
            arguments = bundleOf(FIELD_PAGE to page)
        }
    }
}