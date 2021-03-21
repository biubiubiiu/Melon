package app.melon.home.recommend.ui

import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import app.melon.base.databinding.FragmentEpoxyListBinding
import app.melon.base.framework.BasePagingListFragment
import app.melon.base.utils.showToast
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecommendFragment : BasePagingListFragment() {

    @Inject lateinit var viewModel: RecommendViewModel

    override val controller by lazy(LazyThreadSafetyMode.NONE) {
        RecommendPageController(requireContext())
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

        controller.callbacks = object : RecommendPageController.Actions {
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
        Log.d("raymond", "recommend view created")
        refresh()
    }

    override fun invalidate() {
        // no-op
    }

    override fun onDestroyView() {
        super.onDestroyView()
        controller.clear()
    }

    companion object {
        private const val FIELD_PAGE = "page"

        fun newInstance(page: Int) = RecommendFragment().apply {
            arguments = bundleOf(FIELD_PAGE to page)
        }
    }
}