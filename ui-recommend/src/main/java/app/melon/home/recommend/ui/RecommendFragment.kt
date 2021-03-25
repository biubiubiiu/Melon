package app.melon.home.recommend.ui

import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import app.melon.base.databinding.FragmentEpoxyListBinding
import app.melon.base.framework.BasePagingListFragment
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
        Log.d("raymond", "recommend view created")
        refresh()
    }

    override fun invalidate() {
        // no-op
    }

    companion object {
        private const val FIELD_PAGE = "page"

        fun newInstance(page: Int) = RecommendFragment().apply {
            arguments = bundleOf(FIELD_PAGE to page)
        }
    }
}