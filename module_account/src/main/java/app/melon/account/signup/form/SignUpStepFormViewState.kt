package app.melon.account.signup.form

import app.melon.account.signup.data.SignUpForm

data class SignUpStepFormViewState(
    val username: String? = null,
    val phoneOrEmail: String? = null,
    val birthDate: String? = null,
    val password: String? = null,
    val inputType: ContactInputType = ContactInputType.PHONE_OR_EMAIL,
    val usernameError: Int? = null,
    val phoneOrEmailError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
) {
    private val noError get() = usernameError == null && phoneOrEmailError == null && passwordError == null
    private val dataNotEmpty
        get() = !username.isNullOrEmpty()
                && !phoneOrEmail.isNullOrEmpty()
                && !password.isNullOrEmpty()
                && !birthDate.isNullOrEmpty()

    val form
        get() = if (dataNotEmpty && noError) SignUpForm(
            username = username!!,
            phone = if (inputType == ContactInputType.PHONE) phoneOrEmail!! else "",
            email = if (inputType == ContactInputType.EMAIL) phoneOrEmail!! else "",
            birthDate = birthDate!!,
            password = password!!
        ) else null

    companion object {
        val EMPTY = SignUpStepFormViewState()
    }
}

enum class ContactInputType {
    PHONE_OR_EMAIL, PHONE, EMAIL
}