package app.melon.base.framework

import android.content.Context
import androidx.viewbinding.ViewBinding
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class BaseDaggerFragment<V : ViewBinding> : BaseFragment<V>(), HasAndroidInjector {

    @Inject
    @JvmField
    var androidInjector: DispatchingAndroidInjector<Any>? = null

    override fun androidInjector(): AndroidInjector<Any> = androidInjector!!

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }
}