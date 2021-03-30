package app.melon.account

import android.content.Context
import android.content.Intent
import app.melon.account.api.IAccountService.Observer
import app.melon.account.api.IAccountService
import app.melon.account.login.LoginActivity
import app.melon.account.signup.SignUpStepFormActivity
import javax.inject.Inject

class FakeAccountService @Inject constructor() : IAccountService {

    private var mIsLogin = false
    private val mObservers: MutableList<Observer> = ArrayList()

    private fun FakeAccountService(context: Context) {
        // ...
    }

    override fun startLogin(context: Context) {
        context.startActivity(Intent(context, LoginActivity::class.java))
    }

    override fun startRegister(context: Context) {
        context.startActivity(Intent(context, SignUpStepFormActivity::class.java))
    }

    override val isLogin: Boolean
        get() = mIsLogin

    override fun registerObserver(observer: Observer) {
        if (!mObservers.contains(observer)) {
            mObservers.add(observer)
        }
    }

    override fun unregisterObserver(observer: Observer) {
        mObservers.remove(observer)
    }

    override fun notifyLoginSuccess() {
        mIsLogin = true
        val observers: Array<Observer> = getObservers()
        for (i in observers.indices.reversed()) {
            observers[i].onLoginSuccess()
        }
    }

    override fun notifyLoginCancel() {
        val observers: Array<Observer> = getObservers()
        for (i in observers.indices.reversed()) {
            observers[i].onLoginCancel()
        }
    }

    override fun notifyLoginFailure() {
        val observers: Array<Observer> = getObservers()
        for (i in observers.indices.reversed()) {
            observers[i].onLoginFailure()
        }
    }

    override fun notifyLogout() {
        mIsLogin = false
        val observers: Array<Observer> = getObservers()
        for (i in observers.indices.reversed()) {
            observers[i].onLogout()
        }
    }

    private fun getObservers(): Array<Observer> {
        return mObservers.toTypedArray()
    }
}