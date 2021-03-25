//package app.melon.base
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.MenuItem
//import android.view.ViewGroup
//import android.widget.FrameLayout.LayoutParams
//import androidx.lifecycle.lifecycleScope
//import androidx.recyclerview.widget.DividerItemDecoration
//import androidx.recyclerview.widget.GridLayoutManager
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import androidx.transition.Transition
//import androidx.transition.TransitionManager
//import app.melon.R
//import app.melon.base.framework.FragmentWithBinding
//import app.melon.util.extensions.reverse
//import app.melon.databinding.FragmentNearbyBinding
//import com.google.android.material.transition.MaterialFadeThrough
//import com.google.android.material.transition.MaterialSharedAxis
//import kotlinx.coroutines.flow.collectLatest
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//class TwoTypeFragment : FragmentWithBinding<FragmentNearbyBinding>() {
//
//    @Inject lateinit var nearbyViewModel: NearbyViewModel
//    @Inject lateinit var mAdapter: NearbyAdapter
//
//    private var listTypeGrid = false
//
//    private val listContainer get() = binding.listContainer
//
//    override fun createBinding(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): FragmentNearbyBinding = FragmentNearbyBinding.inflate(inflater)
//
//    override fun onViewCreated(binding: FragmentNearbyBinding, savedInstanceState: Bundle?) {
//        fetchData()
//        setHasOptionsMenu(true)
//        val sharedAxis = MaterialSharedAxis(
//            MaterialSharedAxis.Z,
//            true
//        )
//        setList(listTypeGrid, sharedAxis)
//    }
//
//    /**
//     * Add or replace the RecyclerView containing the list of albums with a new RecyclerView that is
//     * either a list/grid and sorted/unsorted according to the given arguments.
//     */
//    private fun setList(listTypeGrid: Boolean, transition: Transition) {
//        // Use a Transition to animate the removal and addition of the RecyclerViews.
//        val recyclerView = createRecyclerView(listTypeGrid).apply { adapter = mAdapter }
//        val currentRecyclerView = listContainer.getChildAt(0) as? RecyclerView
//        val listState = currentRecyclerView?.layoutManager?.onSaveInstanceState()
//        if (currentRecyclerView != null) {
//            transition.addTarget(currentRecyclerView)
//        }
//        // Restore the RecyclerView's scroll position if available
//        if (listState != null) {
//            recyclerView.layoutManager?.onRestoreInstanceState(listState)
//        }
//        transition.addTarget(recyclerView)
//        TransitionManager.beginDelayedTransition(listContainer, transition)
//        listContainer.removeAllViews()
//        listContainer.addView(recyclerView)
//    }
//
//    private fun createRecyclerView(listTypeGrid: Boolean): RecyclerView {
//        val context = requireContext()
//        val verticalPadding = context.resources.getDimensionPixelSize(R.dimen.spacing_small)
//        return RecyclerView(context).apply {
//            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
//            setPadding(0, verticalPadding, 0, verticalPadding)
//            clipToPadding = false
//            if (listTypeGrid) {
//                layoutManager = GridLayoutManager(context, GRID_SPAN_COUNT)
//            } else {
//                layoutManager = LinearLayoutManager(context)
//                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
//            }
//        }
//    }
//
//    private fun fetchData() {
//        lifecycleScope.launch {
//            nearbyViewModel.getStream().collectLatest {
//                mAdapter.submitData(it)
//            }
//        }
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        this.listTypeGrid = this.listTypeGrid.reverse()
//
//        // Use a fade through transition to swap between list item view types.
//        setList(listTypeGrid, MaterialFadeThrough())
//        return true
//    }
//
//    companion object {
//        private const val GRID_SPAN_COUNT = 2
//    }
//}