package app.melon.home.base

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import app.melon.home.MainViewModel
import dagger.android.support.DaggerFragment

abstract class HomepageToolbarFragment(
    @LayoutRes contentLayoutId: Int
) : DaggerFragment(contentLayoutId) {

    abstract val toolbar: Toolbar

    private val mainViewModel: MainViewModel by activityViewModels()

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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