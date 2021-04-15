package app.melon.user.ui.posts

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import app.melon.util.extensions.ifNotEmpty
import app.melon.util.extensions.viewModelProviderFactoryOf
import app.melon.user.R
import app.melon.user.databinding.FragmentUserPostsBinding
import app.melon.util.delegates.viewBinding
import com.airbnb.epoxy.EpoxyRecyclerView
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class UserPostsFragment : DaggerFragment(R.layout.fragment_user_posts) {

    private val transitionName by lazy { requireArguments().getString(KEY_TRANSITION_NAME, "") }

    @Inject internal lateinit var viewModelFactory: UserPostsViewModel.Factory
    @Inject internal lateinit var controllerFactory: UserPostsController.Factory

    private val binding: FragmentUserPostsBinding by viewBinding()

    private val viewModel: UserPostsViewModel by viewModels {
        viewModelProviderFactoryOf {
            val args = requireArguments()
            viewModelFactory.create(
                id = args.getString(KEY_USER_ID) ?: throw IllegalArgumentException("Missing user id parameter")
            )
        }
    }

    private val controller: UserPostsController by lazy {
        controllerFactory.create(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        binding.recyclerView.setController(controller)

        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(binding.toolbar)
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