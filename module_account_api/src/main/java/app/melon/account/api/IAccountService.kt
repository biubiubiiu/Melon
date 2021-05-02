package app.melon.account.api

import android.content.Context

interface IAccountService {

    val isLogin: Boolean

    val savedUsername: String
    val savePassword: String

    fun startLogin(context: Context)

    fun startRegister(context: Context)

    suspend fun loginUser(): Boolean

    suspend fun loginUser(username: String, password: String): Boolean

    suspend fun logout()

    suspend fun registerUser(username: String, password: String): Boolean

    fun registerObserver(observer: Observer)

    fun unregisterObserver(observer: Observer)

    suspend fun notifyLoginSuccess()

    suspend fun notifyLoginCancel()

    suspend fun notifyLoginFailure()

    suspend fun notifyLogout()

    interface Observer {
        suspend fun onLoginSuccess()
        suspend fun onLoginCancel()
        suspend fun onLoginFailure()
        suspend fun onLogout()
    }
}