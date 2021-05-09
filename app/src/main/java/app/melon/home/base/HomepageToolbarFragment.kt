package app.melon.home.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import app.melon.base.framework.BaseDaggerFragment
import app.melon.home.MainViewModel


abstract class HomepageToolbarFragment<V : ViewBinding> : BaseDaggerFragment<V>() {

    abstract val toolbar: Toolbar

    private val mainViewModel: MainViewModel by activityViewModels()

    @CallSuper
    override fun onViewCreated(binding: V, savedInstanceState: Bundle?) {
        super.onViewCreated(binding, savedInstanceState)
        if (savedInstanceState == null) {
            setupView()
        }
    }

    @CallSuper
    open fun setupView() {
        setupToolbar()
    }

    private fun setupToolbar() {
        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            mainViewModel.openDrawer()
        }
    }
}