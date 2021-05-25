package app.melon.account.signup.review

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.melon.account.R
import app.melon.account.signup.data.SignUpForm
import app.melon.account.signup.state.SignUpResult
import app.melon.base.scope.ActivityScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject


@ActivityScope
internal class SignUpReviewStepViewModel @Inject constructor(
    private val signUpUser: SignUpUser
) : ViewModel() {

    private val _signUpResult = MutableLiveData<SignUpResult>()
    internal val signUpResult: LiveData<SignUpResult> = _signUpResult

    private val _loading = MutableLiveData<Boolean>()
    internal val loading: LiveData<Boolean> = _loading

    init {
        viewModelScope.launch {
            signUpUser.loadingState.observable.collect {
                _loading.postValue(it)
            }
        }
        viewModelScope.launch {
            signUpUser.observe().collect { result ->
                result.onSuccess {
                    _signUpResult.value = SignUpResult(success = true)
                }.onFailure {
                    val errorMessage = determineFailReason(it)
                    _signUpResult.value = SignUpResult(success = false, error = errorMessage)
                }
            }
        }
    }

    internal fun signUp(form: SignUpForm) {
        viewModelScope.launch {
            signUpUser(SignUpUser.Params(form.username, form.password))
        }
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