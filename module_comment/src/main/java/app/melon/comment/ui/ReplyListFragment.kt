package app.melon.comment.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import app.melon.base.databinding.FragmentEpoxyListBinding
import app.melon.base.framework.BasePagingListFragment
import app.melon.util.delegates.observable
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


class ReplyListFragment : BasePagingListFragment() {

    private val id by lazy(LazyThreadSafetyMode.NONE) { arguments?.getString(KEY_COMMENT_ID)!! }

    @Inject internal lateinit var viewModelFactory: ReplyListViewModel.Factory
    @Inject internal lateinit var controllerFactory: ReplyPageController.Factory

    private var firstCommentLoaded by observable(false, { binding.recyclerView.scrollTo(0, 0) })

    private val viewModel by lazy {
        viewModelFactory.create(id)
    }

    override val controller: ReplyPageController by lazy {
        controllerFactory.create(requireContext())
    }

    private var fetchJob: Job? = null

    private fun refresh() {
        fetchJob?.cancel()
        fetchJob = lifecycleScope.launch {
            viewModel.repliesPagingData.collectLatest {
                controller.submitData(it)
            }
        }
    }

    override fun onViewCreated(binding: FragmentEpoxyListBinding, savedInstanceState: Bundle?) {
        super.onViewCreated(binding, savedInstanceState)
        binding.swipeRefreshLayout.isEnabled = false
        initRecyclerView()
        refresh()

        viewModel.liveData.observe(viewLifecycleOwner, Observer {
            controller.firstComment = it.viewComment
            controller.loadingFirstComment = it.loading
        })
        viewModel.liveData.observe(viewLifecycleOwner, Observer {
            if (it.viewComment != null) {
                firstCommentLoaded = true
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initRecyclerView() {
        binding.recyclerView.setOnTouchListener { v, event ->
            when (event.action) {
                // Disallow NestedScrollView to intercept touch events.
                MotionEvent.ACTION_DOWN -> v.parent.requestDisallowInterceptTouchEvent(true)
                // Allow NestedScrollView to intercept touch events.
                MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
            }

            // Handle RecyclerView touch events.
            v.onTouchEvent(event)
            true
        }
    }

    companion object {
        private const val KEY_COMMENT_ID = "KEY_COMMENT_ID"

        fun newInstance(id: String) = ReplyListFragment().apply {
            arguments = bundleOf(KEY_COMMENT_ID to id)
        }
    }
}