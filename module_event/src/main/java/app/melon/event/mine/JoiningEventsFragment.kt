package app.melon.event.mine

import android.content.Context
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.map
import app.melon.base.databinding.FragmentEpoxyListBinding
import app.melon.base.event.TabReselectEvent
import app.melon.base.framework.BasePagingListFragment
import app.melon.event.ViewModelFactory
import app.melon.event.ui.EventsPageController
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


class JoiningEventsFragment : BasePagingListFragment() {

    @Inject internal lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by viewModels<MyEventsViewModel>(::requireActivity) { viewModelFactory }

    private val mPageName get() = arguments?.getString(KEY_PAGE_NAME)
    private var tabReselectEvent: TabReselectEvent? = null

    @Inject internal lateinit var controllerFactory: EventsPageController.Factory
    override val controller by lazy {
        controllerFactory.create(
            context = requireContext()
        )
    }

    private var fetchJob: Job? = null

    private fun refresh() {
        fetchJob?.cancel()
        fetchJob = lifecycleScope.launch {
            viewModel.joiningEvents.collectLatest {
                controller.submitData(it.map { entry -> entry.compoundItem })
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        tabReselectEvent = TabReselectEvent(context) {
            val pageName = it.getStringExtra(TabReselectEvent.PAGE_NAME) ?: ""
            if (pageName == mPageName) {
                binding.recyclerView.smoothScrollToPosition(0)
            }
        }
    }

    override fun onViewCreated(binding: FragmentEpoxyListBinding, savedInstanceState: Bundle?) {
        super.onViewCreated(binding, savedInstanceState)
        binding.recyclerView.setItemSpacingDp(8)
        refresh()
    }

    override fun onDestroy() {
        tabReselectEvent?.dispose()
        super.onDestroy()
    }

    companion object {
        private const val KEY_PAGE_NAME = "KEY_PAGE_NAME"

        fun newInstance(pageName: String) = JoiningEventsFragment().apply {
            arguments = bundleOf(KEY_PAGE_NAME to pageName)
        }
    }
}