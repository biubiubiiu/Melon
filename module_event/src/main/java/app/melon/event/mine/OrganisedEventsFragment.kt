package app.melon.event.mine

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.map
import app.melon.base.databinding.FragmentEpoxyListBinding
import app.melon.base.framework.BasePagingListFragment
import app.melon.event.ViewModelFactory
import app.melon.event.ui.EventsPageController
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


class OrganisedEventsFragment : BasePagingListFragment() {

    @Inject internal lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by viewModels<MyEventsViewModel>(::requireActivity) { viewModelFactory }

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
            viewModel.organisedEvents.collectLatest {
                controller.submitData(it.map { entry -> entry.compoundItem })
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

        fun newInstance(page: Int) = OrganisedEventsFragment().apply {
            arguments = bundleOf(FIELD_PAGE to page)
        }
    }
}