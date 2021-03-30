package app.melon.account.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.melon.account.login.data.LoginRepository
import app.melon.account.R
import app.melon.account.UserManager
import app.melon.account.login.state.LoginFormState
import app.melon.account.login.state.LoginResult
import app.melon.base.scope.ActivityScope
import app.melon.util.base.Success
import kotlinx.coroutines.launch
import javax.inject.Inject


@ActivityScope
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val userManager: UserManager
) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        viewModelScope.launch {
            val result = loginRepository.login(username, password)
            if (result is Success) {
                _loginResult.value = LoginResult(
                    success = result.get()
                )
            } else {
                _loginResult.value = LoginResult(error = R.string.login_failed)
            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (username.isNotBlank() && password.isNotBlank()) {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }
}