package app.melon.composer.selectlocation

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import app.melon.base.ui.extensions.makeInvisible
import app.melon.base.ui.extensions.makeVisible
import app.melon.composer.ComposerViewModel
import app.melon.composer.databinding.FragmentSelectLocationBinding
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


internal class SelectLocationFragment : DaggerFragment() {

    private var _binding: FragmentSelectLocationBinding? = null
    private val binding get() = _binding!!

    private val composerViewModel: ComposerViewModel by activityViewModels()

    @Inject internal lateinit var viewModel: SelectLocationViewModel

    @Inject internal lateinit var factory: LocationAdapter.Factory
    private val adapter by lazy {
        factory.create(::selectLocation, ::clearLocationSelection)
    }

    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val insetsListener = OnApplyWindowInsetsListener { v, insets ->
            val type = WindowInsetsCompat.Type.systemBars()
            val typeInsets = insets.getInsets(type)
            v.setPadding(typeInsets.left, typeInsets.top, typeInsets.right, typeInsets.bottom)
            WindowInsetsCompat.CONSUMED
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.root, insetsListener)

        binding.toolbar.onTrailingAreaClicked = { leavePage() }

        binding.locationResult.also { v ->
            v.setHasFixedSize(true)
            v.addItemDecoration(DividerItemDecoration(v.context, DividerItemDecoration.VERTICAL))
            v.adapter = adapter
        }

        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        search(query)
        initSearch(query)

        viewModel.searchError.observe(viewLifecycleOwner, Observer { message ->
            Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        })

        viewModel.refreshing.observe(viewLifecycleOwner, Observer { refreshing ->
            if (refreshing) {
                binding.refreshing.makeVisible()
            } else {
                binding.refreshing.makeInvisible()
            }
        })
    }

    private fun search(query: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchLocation(query).collectLatest { original ->
                val currentSelection = composerViewModel.locationInfo.value
                val selectionIndex = original.indexOfFirst { it == currentSelection }
                val result = original.mapIndexed { index, poiInfo ->
                    LocationWithSelection(poiInfo, isSelected = index == selectionIndex)
                }
                adapter.submitList(result) {
                    binding.locationResult.scrollToPosition(0)
                }
            }
        }
    }

    private fun initSearch(query: String) {
        binding.searchKeyword.setText(query)

        binding.searchKeyword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateLocationResultFromInput()
                true
            } else {
                false
            }
        }
        binding.searchKeyword.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateLocationResultFromInput()
                true
            } else {
                false
            }
        }
    }

    private fun updateLocationResultFromInput() {
        binding.searchKeyword.text.trim().let {
            search(it.toString())
        }
    }

    private fun selectLocation(selection: LocationWithSelection) {
        composerViewModel.locationChanged(selection.info)
        leavePage()
    }

    private fun clearLocationSelection() {
        composerViewModel.locationChanged(null)
        leavePage()
    }

    private fun leavePage() {
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val LAST_SEARCH_QUERY = "last_search_query"
        private const val DEFAULT_QUERY = ""
    }
}