package app.melon.account.signup.review

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.melon.account.R
import app.melon.account.api.UserManager
import app.melon.account.signup.data.SignUpForm
import app.melon.account.signup.state.SignUpResult
import app.melon.base.scope.ActivityScope
import app.melon.util.base.ErrorResult
import app.melon.util.base.Success
import app.melon.util.encrypt.EncryptUtils
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject


@ActivityScope
class SignUpReviewStepViewModel @Inject constructor(
    private val userManager: UserManager,
    private val signUpUser: SignUpUser
) : ViewModel() {

    private val _signUpResult = MutableLiveData<SignUpResult>()
    val signUpResult: LiveData<SignUpResult> = _signUpResult

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    init {
        viewModelScope.launch {
            signUpUser.loadingState.observable.collect {
                _loading.postValue(it)
            }
        }
        viewModelScope.launch {
            signUpUser.observe().collect {
                if (it is Success) {
                    _signUpResult.value = SignUpResult(success = true)
                } else if (it is ErrorResult) {
                    val errorMessage = determineFailReason(it.throwable)
                    _signUpResult.value = SignUpResult(success = false, error = errorMessage)
                }
            }
        }
    }

    fun signUp(form: SignUpForm) {
        viewModelScope.launch {
            signUpUser(SignUpUser.Params(form.username, form.password))
        }
    }

    fun saveUser(form: SignUpForm) {
        userManager.registerUser(
            username = form.username,
            password = EncryptUtils.getSHA512HashOfString(form.password)
        )
    }

    @StringRes
    private fun determineFailReason(throwable: Throwable): Int {
        return if (throwable is HttpException) {
            when (throwable.code()) {
                201 -> R.string.account_error_hint_user_already_registered
                401 -> R.string.account_error_hint_unauthorized
                403 -> R.string.account_error_hint_forbidden
                404 -> R.string.account_error_hint_not_found
                else -> R.string.account_error_hint_unknown_error
            }
        } else {
            R.string.account_error_hint_unknown_error
        }
    }
}