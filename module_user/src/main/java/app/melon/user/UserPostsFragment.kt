package app.melon.user

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.lifecycle.lifecycleScope
import app.melon.base.utils.extensions.ifNotEmpty
import com.airbnb.epoxy.EpoxyRecyclerView
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class UserPostsFragment : DaggerFragment(R.layout.fragment_user_posts) {

    private val uid by lazy { requireArguments().getString(KEY_USER_ID, "") }
    private val transitionName by lazy { requireArguments().getString(KEY_TRANSITION_NAME, "") }

    @Inject lateinit var viewModel: UserPostsViewModel

    private val controller: UserPostsController by lazy(LazyThreadSafetyMode.NONE) {
        UserPostsController(requireContext())
    }

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setUid(uid)

        lifecycleScope.launchWhenCreated {
            viewModel.pagingData.collectLatest {
                controller.submitData(it)
            }
        }
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        transitionName.ifNotEmpty { ViewCompat.setTransitionName(view, it) }

        val recyclerView: EpoxyRecyclerView = view.findViewById(R.id.user_post_list)
        recyclerView.setController(controller)

        toolbar = view.findViewById(R.id.toolbar)
        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_search).isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

    companion object {
        private const val KEY_USER_ID = "KEY_USER_ID"
        private const val KEY_TRANSITION_NAME = "KEY_TRANSITION_NAME"

        fun newInstance(
            uid: String,
            transitionName: String
        ): UserPostsFragment =
            UserPostsFragment().apply {
                arguments = bundleOf(
                    KEY_USER_ID to uid,
                    KEY_TRANSITION_NAME to transitionName
                )
            }
    }
}