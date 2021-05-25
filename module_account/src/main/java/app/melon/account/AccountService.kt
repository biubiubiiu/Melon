package app.melon.account

import android.content.Context
import android.content.Intent
import app.melon.account.api.IAccountService.Observer
import app.melon.account.api.IAccountService
import app.melon.account.data.LoginStatus
import app.melon.account.login.LoginActivity
import app.melon.account.login.data.LoginRepository
import app.melon.account.signup.SignUpStepFormActivity
import app.melon.util.encrypt.EncryptUtils
import app.melon.util.network.TokenManager
import app.melon.util.storage.RegistrationStorage
import app.melon.util.storage.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
internal class AccountService @Inject constructor(
    @RegistrationStorage private val storage: Storage,
    private val repo: LoginRepository,
    private val tokenManager: TokenManager
) : IAccountService {

    companion object {
        private const val REGISTERED_USER = "registered_user"
        private const val PASSWORD_SUFFIX = "password"
    }

    private var mIsLogin = false
    private val mObservers: MutableList<Observer> = ArrayList()

    override val savedUsername: String get() = storage.getString(REGISTERED_USER)
    override val savedPassword: String get() = storage.getString("$savedUsername${PASSWORD_SUFFIX}")

    override fun startLogin(context: Context) {
        context.startActivity(Intent(context, LoginActivity::class.java))
    }

    override fun startRegister(context: Context) {
        context.startActivity(Intent(context, SignUpStepFormActivity::class.java))
    }

    override suspend fun loginUser(): Boolean {
        return loginUser(savedUsername, savedPassword, encrypt = false)
    }

    override suspend fun loginUser(username: String, password: String): Boolean {
        return loginUser(username, password, encrypt = true)
    }

    private suspend fun loginUser(username: String, password: String, encrypt: Boolean): Boolean {
        val encryptedPassword = if (encrypt) {
            EncryptUtils.getSHA512HashOfString(password)
        } else {
            password // password has been encrypted
        }
        val result = withContext(Dispatchers.IO) {
            repo.login(username, encryptedPassword)
        }
        return when (result) {
            is LoginStatus.Success -> {
                tokenManager.updateToken(result.token)
                saveUser(username, encryptedPassword)
                notifyLoginSuccess()
                true
            }
            else -> {
                notifyLoginFailure()
                false
            }
        }
    }

    override suspend fun logout() {
        tokenManager.clearToken()
        notifyLogout()
    }

    override suspend fun registerUser(username: String, password: String): Boolean {
        val encryptedPassword = EncryptUtils.getSHA512HashOfString(password)
        return withContext(Dispatchers.IO) {
            repo.signUp(username, encryptedPassword)
        }.fold(
            onSuccess = { true },
            onFailure = { false }
        )
    }

    private fun saveUser(username: String, password: String) {
        storage.setString(REGISTERED_USER, username)
        storage.setString("$username${PASSWORD_SUFFIX}", password)
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

    override suspend fun notifyLoginSuccess() {
        mIsLogin = true
        val observers: Array<Observer> = getObservers()
        for (i in observers.indices.reversed()) {
            observers[i].onLoginSuccess()
        }
    }

    override suspend fun notifyLoginCancel() {
        val observers: Array<Observer> = getObservers()
        for (i in observers.indices.reversed()) {
            observers[i].onLoginCancel()
        }
    }

    override suspend fun notifyLoginFailure() {
        val observers: Array<Observer> = getObservers()
        for (i in observers.indices.reversed()) {
            observers[i].onLoginFailure()
        }
    }

    override suspend fun notifyLogout() {
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