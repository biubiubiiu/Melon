package app.melon.base.framework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import app.melon.base.databinding.FragmentEpoxyListBinding
import app.melon.base.lazyload.LazyLoadFragment
import com.airbnb.epoxy.EpoxyController


abstract class BaseEpoxyListFragment : LazyLoadFragment<FragmentEpoxyListBinding>() {

    abstract val controller: EpoxyController

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentEpoxyListBinding = FragmentEpoxyListBinding.inflate(inflater, container, false)

    @CallSuper
    override fun onViewCreated(binding: FragmentEpoxyListBinding, savedInstanceState: Bundle?) {
        super.onViewCreated(binding, savedInstanceState)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.setController(controller)
    }
}