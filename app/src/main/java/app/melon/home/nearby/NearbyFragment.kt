package app.melon.home.nearby

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import app.melon.R
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class NearbyFragment : DaggerFragment() {

    @Inject lateinit var nearbyViewModel: NearbyViewModel
    @Inject lateinit var adapter: NearbyAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nearby, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<RecyclerView>(R.id.list).adapter = adapter
        fetchData()
    }

    private fun fetchData() {
        lifecycleScope.launch {
            nearbyViewModel.getStream().collectLatest {
                adapter.submitData(it)
            }
        }
    }
}