package app.melon.home.following.ui

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import app.melon.base.databinding.FragmentEpoxyListBinding
import app.melon.base.framework.BaseMvRxEpoxyFragment
import app.melon.base.framework.BasePagingListFragment
import app.melon.base.utils.showToast
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class FollowFragment : BasePagingListFragment() {

    @Inject lateinit var viewModel: FollowViewModel

    override val controller by lazy(LazyThreadSafetyMode.NONE) {
        FollowPageController(requireContext())
    }

    private var fetchJob: Job? = null

    private fun refresh() {
        fetchJob?.cancel()
        fetchJob = lifecycleScope.launch {
            viewModel.refresh(timestamp = 1000000).collectLatest {
                controller.submitData(it)
            }
        }
    }

    private val pageIndex by lazy(LazyThreadSafetyMode.NONE) { arguments?.getInt(FIELD_PAGE, -1) }

    override fun onViewCreated(binding: FragmentEpoxyListBinding, savedInstanceState: Bundle?) {
        super.onViewCreated(binding, savedInstanceState)
        binding.recyclerView.setItemSpacingDp(8)

        controller.callbacks = object : FollowPageController.Actions {
            override fun onHolderClick() {
                context?.showToast("click holder")
            }

            override fun onAvatarClick() {
                context?.showToast("click avatar")
            }

            override fun onShareClick() {
                context?.showToast("click share")
            }

            override fun onCommentClick() {
                context?.showToast("click comment")
            }

            override fun onFavorClick() {
                context?.showToast("click favor")
            }

            override fun onMoreClick() {
                context?.showToast("click more")
            }
        }

        refresh()
    }

    override fun invalidate() {
        // No-op
    }

    override fun onDestroyView() {
        super.onDestroyView()
        controller.clear()
    }

    companion object {
        private const val FIELD_PAGE = "page"

        fun newInstance(page: Int) = FollowFragment().apply {
            arguments = bundleOf(FIELD_PAGE to page)
        }
    }
}