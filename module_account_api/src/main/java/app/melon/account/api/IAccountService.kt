package app.melon.account.api

import android.content.Context

interface IAccountService {

    val isLogin: Boolean

    fun startLogin(context: Context)

    fun startRegister(context: Context)

    fun registerObserver(observer: Observer)

    fun unregisterObserver(observer: Observer)

    fun notifyLoginSuccess()

    fun notifyLoginCancel()

    fun notifyLoginFailure()

    fun notifyLogout()

    interface Observer {
        fun onLoginSuccess()
        fun onLoginCancel()
        fun onLoginFailure()
        fun onLogout()
    }
}