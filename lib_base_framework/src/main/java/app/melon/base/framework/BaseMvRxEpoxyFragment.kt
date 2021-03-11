package app.melon.base.framework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import app.melon.base.databinding.FragmentEpoxyListBinding
import com.airbnb.epoxy.EpoxyController
import com.airbnb.mvrx.MavericksView

abstract class BaseMvRxEpoxyFragment : FragmentWithBinding<FragmentEpoxyListBinding>(), MavericksView {

    abstract val controller: EpoxyController

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentEpoxyListBinding = FragmentEpoxyListBinding.inflate(inflater, container, false)

    @CallSuper
    override fun onViewCreated(binding: FragmentEpoxyListBinding, savedInstanceState: Bundle?) {
        binding.recyclerView.setControllerAndBuildModels(controller)
    }

    protected fun rebuildModel() {
        binding.recyclerView.requestModelBuild()
    }
}