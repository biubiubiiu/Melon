package app.melon.account.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.melon.account.R
import app.melon.account.api.IAccountService
import app.melon.account.login.state.LoginFormState
import app.melon.account.login.state.LoginResult
import app.melon.base.scope.ActivityScope
import kotlinx.coroutines.launch
import javax.inject.Inject


@ActivityScope
internal class LoginViewModel @Inject constructor(
    private val accountService: IAccountService
) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    internal val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    internal val loginResult: LiveData<LoginResult> = _loginResult

    internal fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        viewModelScope.launch {
            val result = accountService.loginUser(username, password)
            _loginResult.value = if (result) {
                LoginResult(success = true)
            } else {
                LoginResult(error = R.string.login_failed)
            }
        }
    }

    internal fun loginDataChanged(username: String, password: String) {
        if (username.isNotBlank() && password.isNotBlank()) {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }
}