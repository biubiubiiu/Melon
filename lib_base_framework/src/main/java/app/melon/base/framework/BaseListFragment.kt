package app.melon.base.framework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.melon.base.databinding.FragmentListBinding
import com.airbnb.mvrx.MavericksView

abstract class BaseListFragment : Fragment(), MavericksView {

    private var _binding: FragmentListBinding? = null
    private val binding: FragmentListBinding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}