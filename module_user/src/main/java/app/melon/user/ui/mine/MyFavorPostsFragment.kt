package app.melon.user.ui.mine

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import app.melon.base.databinding.FragmentEpoxyListBinding
import app.melon.base.framework.BasePagingListFragment
import app.melon.feed.anonymous.ui.FeedPageController
import app.melon.feed.anonymous.ui.SchoolFeedFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class MyFavorPostsFragment : BasePagingListFragment() {

    @Inject internal lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by viewModels<MyProfileViewModel>(::requireActivity) { viewModelFactory }

    @Inject internal lateinit var controllerFactory: FeedPageController.Factory

    override val controller by lazy {
        controllerFactory.create(
            context = requireContext(),
            idProvider = { feed, _ -> "anonymous_school_feeds_${feed!!.feedId}" }
        )
    }

    private var fetchJob: Job? = null

    private fun refresh() {
        fetchJob?.cancel()
        fetchJob = lifecycleScope.launch {
            viewModel.myFavorFeedsPagingData.collectLatest {
                controller.submitData(it)
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