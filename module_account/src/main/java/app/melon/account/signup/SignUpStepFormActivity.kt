package app.melon.account.signup

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import app.melon.account.R
import app.melon.account.databinding.ActivitySignUpStepFormBinding
import app.melon.account.signup.form.ContactInputType
import app.melon.account.signup.form.SignUpStepFormViewModel
import app.melon.account.signup.form.SignUpStepFormViewState
import app.melon.util.extensions.afterTextChanged
import app.melon.util.extensions.resolveTheme
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class SignUpStepFormActivity : DaggerAppCompatActivity() {

    @Inject internal lateinit var viewModel: SignUpStepFormViewModel

    private lateinit var binding: ActivitySignUpStepFormBinding

    private var hasFocusedOnUsername = false
    private var hasFocusedOnPassword = false
    private var hasFocusedOnContact = false

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            when (result.resultCode) {
                RESULT_FOCUS_ON_USERNAME -> binding.signUpInputUsernameContent.requestFocus()
                RESULT_FOCUS_ON_PASSWORD -> binding.signUpInputPasswordContent.requestFocus()
                RESULT_FOCUS_ON_PHONE_OR_EMAIL -> binding.signUpInputEmailOrPhoneContent.requestFocus()
                RESULT_FOCUS_ON_BIRTH_DATE -> binding.signUpInputBirthDateContent.requestFocus()
            }
        }

    companion object {
        const val RESULT_FOCUS_ON_USERNAME = 100
        const val RESULT_FOCUS_ON_PASSWORD = 101
        const val RESULT_FOCUS_ON_PHONE_OR_EMAIL = 102
        const val RESULT_FOCUS_ON_BIRTH_DATE = 103
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpStepFormBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupToolbar()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setupUsernameInput()
        setupContactInfoInput()
        setupPasswordInput()
        setupBirthDateInput()
        setupValidation()
        setupNextStep()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun setupUsernameInput() {
        binding.signUpInputUsernameContent.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                hasFocusedOnUsername = true
            }
        }
        viewModel.selectObserve(SignUpStepFormViewState::usernameError).observe(this, Observer {
            if (hasFocusedOnUsername) {
                binding.signUpInputUsername.error = if (it == null) it else getString(it)
            }
        })
    }

    private fun setupContactInfoInput() {
        binding.signUpInputEmailOrPhoneContent.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                viewModel.updateContactInputType()
                hasFocusedOnContact = true
            }
        }
        binding.signUpStepFormBottomHint.setOnClickListener {
            viewModel.toggleInputType()
        }
        viewModel.selectObserve(SignUpStepFormViewState::inputType).observe(this, Observer {
            binding.signUpInputEmailOrPhone.hint = when (it!!) {
                ContactInputType.PHONE_OR_EMAIL -> getString(R.string.account_hint_email_or_phone)
                ContactInputType.EMAIL -> getString(R.string.account_hint_email)
                ContactInputType.PHONE -> getString(R.string.account_hint_phone)
            }
            binding.signUpInputEmailOrPhoneContent.inputType = when (it) {
                ContactInputType.PHONE_OR_EMAIL -> InputType.TYPE_CLASS_PHONE
                ContactInputType.EMAIL -> InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                ContactInputType.PHONE -> InputType.TYPE_CLASS_PHONE
            }
            binding.signUpStepFormBottomHint.text = when (it) {
                ContactInputType.PHONE_OR_EMAIL -> ""
                ContactInputType.EMAIL -> getString(R.string.account_prompt_user_phone_instead)
                ContactInputType.PHONE -> getString(R.string.account_prompt_user_email_instead)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val autoFillHint = when (it) {
                    ContactInputType.EMAIL -> View.AUTOFILL_HINT_EMAIL_ADDRESS
                    ContactInputType.PHONE_OR_EMAIL, ContactInputType.PHONE -> View.AUTOFILL_HINT_PHONE
                }
                binding.signUpInputEmailOrPhoneContent.setAutofillHints(autoFillHint)
            }
        })
        viewModel.selectObserve(SignUpStepFormViewState::phoneOrEmailError).observe(this, Observer {
            if (hasFocusedOnContact) {
                binding.signUpInputEmailOrPhone.error = if (it == null) it else getString(it)
            }
        })
    }

    private fun setupPasswordInput() {
        binding.signUpInputPasswordContent.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                hasFocusedOnPassword = true
            }
        }
        viewModel.selectObserve(SignUpStepFormViewState::passwordError).observe(this, Observer {
            if (hasFocusedOnPassword) {
                binding.signUpInputPassword.error = if (it == null) it else getString(it)
            }
        })
    }

    private fun setupBirthDateInput() {
        binding.signUpInputBirthDateContent.inputType = InputType.TYPE_NULL
        binding.signUpInputBirthDateContent.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val builder = setupDateSelectorBuilder(this)
                val picker = builder.build()
                picker.addOnPositiveButtonClickListener {
                    binding.signUpInputBirthDateContent.setText(picker.headerText)
                }
                picker.showNow(supportFragmentManager, picker.toString())
            }
        }
    }

    private fun setupValidation() {
        binding.signUpInputUsernameContent.afterTextChanged {
            viewModel.formDataChanged(
                it,
                binding.signUpInputEmailOrPhoneContent.text?.toString() ?: "",
                binding.signUpInputBirthDateContent.text.toString(),
                binding.signUpInputPasswordContent.text?.toString() ?: ""
            )
        }
        binding.signUpInputEmailOrPhoneContent.afterTextChanged {
            viewModel.formDataChanged(
                binding.signUpInputUsernameContent.text?.toString() ?: "",
                it,
                binding.signUpInputBirthDateContent.text.toString(),
                binding.signUpInputPasswordContent.text?.toString() ?: ""
            )
        }
        binding.signUpInputBirthDateContent.afterTextChanged {
            viewModel.formDataChanged(
                binding.signUpInputUsernameContent.text?.toString() ?: "",
                binding.signUpInputEmailOrPhoneContent.text?.toString() ?: "",
                it,
                binding.signUpInputPasswordContent.text?.toString() ?: ""
            )
        }
        binding.signUpInputPasswordContent.afterTextChanged {
            viewModel.formDataChanged(
                binding.signUpInputUsernameContent.text?.toString() ?: "",
                binding.signUpInputEmailOrPhoneContent.text?.toString() ?: "",
                binding.signUpInputBirthDateContent.text.toString(),
                it
            )
        }
    }

    private fun setupNextStep() {
        viewModel.selectObserve(SignUpStepFormViewState::isDataValid).observe(this, Observer {
            binding.signUpStepFormNextStep.isEnabled = it
        })
        binding.signUpStepFormNextStep.setOnClickListener {
            val form = viewModel.currentState().form!!
            val intent = SignUpReviewStepActivity.prepareIntent(this, form)
            startForResult.launch(intent)
        }
    }

    private fun setupDateSelectorBuilder(
        context: Context
    ) = MaterialDatePicker.Builder.datePicker().apply {
        setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
        setTheme(context.resolveTheme(R.attr.materialCalendarTheme))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}