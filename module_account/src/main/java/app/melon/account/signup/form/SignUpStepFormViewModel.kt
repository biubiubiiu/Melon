package app.melon.account.signup.form

import android.util.Patterns
import androidx.lifecycle.viewModelScope
import app.melon.account.R
import app.melon.base.domain.SuspendingWorkInteractor
import app.melon.base.framework.ReduxViewModel
import app.melon.base.scope.ActivityScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.regex.Pattern
import javax.inject.Inject

/**
 * SignUpStepFormViewModel is the ViewModel that the Registration flow ([app.melon.account.signup.SignUpStepFormActivity])
 * uses to keep user's input data.
 */
@ActivityScope
class SignUpStepFormViewModel @Inject constructor(
    private val validateUsername: ValidateUsername,
    private val validatePhone: ValidatePhone,
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword
) : ReduxViewModel<SignUpStepFormViewState>(
    initialState = SignUpStepFormViewState.EMPTY
) {

    private val usernameChannel = Channel<String>()
    private val usernameFlow = usernameChannel.receiveAsFlow()

    private val passwordChannel = Channel<String>()
    private val passwordFlow = passwordChannel.receiveAsFlow()

    private val phoneOrEmailChannel = Channel<String>()
    private val phoneOrEmailFlow = phoneOrEmailChannel.receiveAsFlow()

    private val contactTypeChannel = Channel<ContactInputType>()
    private val contactTypeFlow = contactTypeChannel.receiveAsFlow()

    private val birthDateChannel = Channel<String>()
    private val birthDateFlow = birthDateChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            usernameFlow.conflate().distinctUntilChanged().collectLatest {
                setState {
                    copy(username = it)
                }
                validateUsername(it)
            }
        }
        viewModelScope.launch {
            passwordFlow.conflate().distinctUntilChanged().collectLatest {
                setState {
                    copy(password = it)
                }
                validatePassword(it)
            }
        }
        viewModelScope.launch {
            phoneOrEmailFlow
                .combine(contactTypeFlow) { input, type ->
                    input to type
                }
                .conflate()
                .distinctUntilChanged()
                .collectLatest { (input, type) ->
                    setState {
                        copy(phoneOrEmail = input, inputType = type)
                    }
                    if (type == ContactInputType.EMAIL) validateEmail(input) else validatePhone(input)
                }
        }
        viewModelScope.launch {
            birthDateFlow.conflate().distinctUntilChanged().collectLatest {
                setState {
                    copy(birthDate = it)
                }
            }
        }
        viewModelScope.launch {
            validateUsername.observe().collectAndSetState {
                copy(usernameError = it)
            }
        }
        viewModelScope.launch {
            validatePassword.observe().collectAndSetState {
                copy(passwordError = it)
            }
        }
        viewModelScope.launch {
            validateEmail.observe().collectAndSetState {
                copy(phoneOrEmailError = it)
            }
        }
        viewModelScope.launch {
            validatePhone.observe().collectAndSetState {
                copy(phoneOrEmailError = it)
            }
        }
        selectSubscribe(SignUpStepFormViewState::form) {
            viewModelScope.launchSetState {
                copy(isDataValid = it != null)
            }
        }
    }

    fun formDataChanged(
        username: String,
        phoneOrEmail: String,
        birthDate: String,
        password: String
    ) {
        viewModelScope.launch {
            usernameChannel.send(username)
            phoneOrEmailChannel.send(phoneOrEmail)
            passwordChannel.send(password)
            birthDateChannel.send(birthDate)
        }
    }

    fun updateContactInputType() {
        if (currentState().inputType != ContactInputType.PHONE_OR_EMAIL) {
            return
        }
        viewModelScope.launch {
            val nextState = ContactInputType.PHONE
            contactTypeChannel.send(nextState)
        }
    }

    fun toggleInputType() {
        viewModelScope.launch {
            val nextState = when (currentState().inputType) {
                ContactInputType.PHONE -> ContactInputType.EMAIL
                else -> ContactInputType.PHONE
            }
            contactTypeChannel.send(nextState)
        }
    }
}

class ValidateUsername @Inject constructor() : SuspendingWorkInteractor<String, Int?>() {

    companion object {
        private const val USERNAME_MAX_LENGTH = 30
    }

    override suspend fun doWork(params: String): Int? {
        return withContext(Dispatchers.Default) {
            when {
                params.isBlank() -> R.string.account_error_hint_blank_username
                params.length > USERNAME_MAX_LENGTH -> R.string.account_error_hint_username_over_long
                else -> null
            }
        }
    }
}

class ValidatePhone @Inject constructor() : SuspendingWorkInteractor<String, Int?>() {

    companion object {
        private const val PHONE_PATTERN =
            "^[1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|([6][5,6])|([7][0-8])|([8][0-9])|([9][189]))[0-9]{8}\$"
        private val PHONE: Pattern = Pattern.compile(PHONE_PATTERN)
    }

    override suspend fun doWork(params: String): Int? {
        return withContext(Dispatchers.Default) {
            if (PHONE.matcher(params).matches()) {
                null
            } else {
                R.string.account_error_hint_invalid_phone
            }
        }
    }
}

class ValidateEmail @Inject constructor() : SuspendingWorkInteractor<String, Int?>() {

    override suspend fun doWork(params: String): Int? {
        return withContext(Dispatchers.Default) {
            if (Patterns.EMAIL_ADDRESS.matcher(params).matches()) {
                null
            } else {
                R.string.account_error_hint_invalid_email
            }
        }
    }
}

class ValidatePassword @Inject constructor() : SuspendingWorkInteractor<String, Int?>() {
    companion object {
        private const val PASSWORD_PATTERN =
            "^((?=.*[\\d])(?=.*[a-z])|(?=.*[A-Z])(?=.*[a-z])|(?=.*[\\d])(?=.*[A-Z])).{6,16}\$"
        private val PASSWORD = Pattern.compile(PASSWORD_PATTERN)
    }

    override suspend fun doWork(params: String): Int? {
        return withContext(Dispatchers.Default) {
            if (PASSWORD.matcher(params).matches()) {
                null
            } else {
                R.string.account_error_hint_invalid_password
            }
        }
    }
}
