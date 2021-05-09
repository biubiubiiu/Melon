package app.melon.base.framework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import app.melon.base.databinding.FragmentEpoxyListBinding
import app.melon.base.lazyload.LazyLoadFragment
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.Typed2EpoxyController
import com.airbnb.epoxy.Typed3EpoxyController
import com.airbnb.epoxy.Typed4EpoxyController
import com.airbnb.epoxy.TypedEpoxyController


abstract class BaseEpoxyListFragment : LazyLoadFragment<FragmentEpoxyListBinding>() {

    abstract val controller: EpoxyController

    private val allowModelBuildRequests
        get() = controller !is TypedEpoxyController<*> &&
                controller !is Typed2EpoxyController<*, *> &&
                controller !is Typed3EpoxyController<*, *, *> &&
                controller !is Typed4EpoxyController<*, *, *, *>

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentEpoxyListBinding = FragmentEpoxyListBinding.inflate(inflater, container, false)

    @CallSuper
    override fun onViewCreated(binding: FragmentEpoxyListBinding, savedInstanceState: Bundle?) {
        super.onViewCreated(binding, savedInstanceState)
        binding.recyclerView.setHasFixedSize(true)

        if (allowModelBuildRequests) {
            binding.recyclerView.setControllerAndBuildModels(controller)
        } else {
            binding.recyclerView.setController(controller)
        }
    }
}