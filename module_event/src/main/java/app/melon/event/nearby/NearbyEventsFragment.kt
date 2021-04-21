package app.melon.event.nearby

import android.os.Bundle
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


class NearbyEventsFragment : BasePagingListFragment() {

    @Inject internal lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by viewModels<NearbyEventsViewModel>(::requireActivity) { viewModelFactory }

    @Inject internal lateinit var controllerFactory: EventsPageController.Factory
    override val controller by lazy {
        controllerFactory.create(requireContext())
    }

    private var fetchJob: Job? = null

    private fun refresh() {
        fetchJob?.cancel()
        fetchJob = lifecycleScope.launch {
            viewModel.eventsPagingData.collectLatest {
                controller.submitData(it.map { entry -> entry.compoundItem })
            }
        }
    }

    override fun onViewCreated(binding: FragmentEpoxyListBinding, savedInstanceState: Bundle?) {
        super.onViewCreated(binding, savedInstanceState)
        binding.recyclerView.setItemSpacingDp(8)
        refresh()
    }
}