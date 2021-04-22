package app.melon.feed.ui

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import app.melon.base.databinding.FragmentEpoxyListBinding
import app.melon.base.event.TabReselectEvent
import app.melon.base.framework.BasePagingListFragment
import app.melon.data.constants.FeedPageType
import app.melon.feed.FeedPageConfig
import app.melon.feed.ui.controller.FeedPageController
import app.melon.util.extensions.showToast
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


class CommonFeedFragment : BasePagingListFragment() {

    private val pageType get() = arguments?.getInt(KEY_FEED_PAGE_TYPE, -1) ?: -1
    private val mPageName get() = arguments?.getString(KEY_PAGE_NAME)
    private val itemIdPrefix get() = arguments?.getString(KEY_ID_PREFIX) ?: ""
    private val isAnonymousFeed get() = arguments?.getBoolean(KEY_IS_ANONYMOUS, false) ?: false

    private val extraParam get() = arguments?.getBundle(KEY_EXTRA_PARAM)

    private var tabReselectEvent: TabReselectEvent? = null

    @Inject internal lateinit var controllerFactory: FeedPageController.Factory
    override val controller by lazy {
        controllerFactory.create(
            context = requireContext(),
            idProvider = { _, position -> "${itemIdPrefix}_$position" },
            type = if (isAnonymousFeed) FeedPageController.Type.ANONYMOUS else FeedPageController.Type.NORMAL
        )
    }

    @Inject internal lateinit var viewModel: FeedListViewModel

    private var fetchJob: Job? = null

    private fun refresh() {
        fetchJob?.cancel()
        fetchJob = lifecycleScope.launch {
            viewModel.refresh(pageType, extraParam).collectLatest {
                controller.submitData(it)
            }
        }
    }

    override fun onFirstVisible() {
        super.onFirstVisible()
        if (isArgumentsValid()) {
            refresh()
        } else {
            context?.showToast("Invalid Arguments with pageIndex = [$mPageName], pageType = [$pageType], prefix = [$itemIdPrefix]")
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
        if (savedInstanceState == null) {
            binding.recyclerView.setItemSpacingDp(8)
        }
    }

    override fun onDestroy() {
        tabReselectEvent?.dispose()
        super.onDestroy()
    }

    private fun isArgumentsValid(): Boolean {
        return pageType != -1
                && itemIdPrefix.isNotEmpty()
    }

    companion object {
        private const val KEY_PAGE_NAME = "KEY_PAGE_NAME"
        private const val KEY_FEED_PAGE_TYPE = "KEY_FEED_PAGE_TYPE"
        private const val KEY_ID_PREFIX = "KEY_ID_PREFIX"
        private const val KEY_IS_ANONYMOUS = "KEY_FEED_IS_ANONYMOUS" // TODO move this field to [Feed]
        private const val KEY_EXTRA_PARAM = "KEY_EXTRA_PARAM"

        fun newInstance(
            pageName: String,
            config: FeedPageConfig,
            extraParams: Bundle? = null
        ) = newInstance(
            pageName,
            config.pageType,
            config.idPrefix,
            config.isAnonymousFeed,
            extraParams
        )

        fun newInstance(
            pageName: String,
            @FeedPageType pageType: Int,
            idPrefix: String,
            isAnonymousFeed: Boolean,
            extraParams: Bundle? = null
        ) = CommonFeedFragment().apply {
            arguments = Bundle().apply {
                putString(KEY_PAGE_NAME, pageName)
                putInt(KEY_FEED_PAGE_TYPE, pageType)
                putString(KEY_ID_PREFIX, idPrefix)
                putBoolean(KEY_IS_ANONYMOUS, isAnonymousFeed)
                putBundle(KEY_EXTRA_PARAM, extraParams)
            }
        }
    }
}