package app.melon.account.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import app.melon.account.R
import app.melon.account.databinding.ActivitySignUpStepReviewBinding
import app.melon.account.login.LoginActivity
import app.melon.account.signup.data.SignUpForm
import app.melon.account.signup.review.SignUpReviewStepViewModel
import app.melon.util.extensions.clearTop
import app.melon.util.extensions.showToast
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class SignUpReviewStepActivity : DaggerAppCompatActivity(R.layout.activity_sign_up_step_review) {

    private val form: SignUpForm by lazy(LazyThreadSafetyMode.NONE) { intent.getParcelableExtra<SignUpForm>(KEY_FORM)!! }

    @Inject internal lateinit var viewModel: SignUpReviewStepViewModel

    private lateinit var binding: ActivitySignUpStepReviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpStepReviewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupToolbar()

        binding.signUpReviewUsername.text = form.username
        binding.signUpReviewPassword.text = form.password
        binding.signUpReviewPhoneOrEmail.text = if (form.phone.isNotBlank()) {
            form.phone
        } else {
            form.email
        }
        binding.signUpReviewBirthDate.text = form.birthDate

        binding.signUpReviewUsername.setOnClickListener {
            setResult(SignUpStepFormActivity.RESULT_FOCUS_ON_USERNAME)
            finish()
        }
        binding.signUpReviewPassword.setOnClickListener {
            setResult(SignUpStepFormActivity.RESULT_FOCUS_ON_PASSWORD)
            finish()
        }
        binding.signUpReviewPhoneOrEmail.setOnClickListener {
            setResult(SignUpStepFormActivity.RESULT_FOCUS_ON_PHONE_OR_EMAIL)
            finish()
        }
        binding.signUpReviewBirthDate.setOnClickListener {
            setResult(SignUpStepFormActivity.RESULT_FOCUS_ON_BIRTH_DATE)
            finish()
        }

        binding.signUp.setOnClickListener {
            viewModel.signUp(form)
        }

        viewModel.signUpResult.observe(this, Observer {
            if (it.error != null) {
                showSignUpFailed(it.error)
            } else if (it.success) {
                viewModel.saveUser(form)
                val intent = LoginActivity.prepareIntent(this, form.username, form.password)
                    .clearTop()
                startActivity(intent)
            }
        })

        viewModel.loading.observe(this, Observer {
            binding.reviewLoading.isVisible = it
        })
    }

    private fun showSignUpFailed(@StringRes message: Int) {
        this.showToast(message)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
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

    companion object {
        private const val KEY_FORM = "KEY_FORM"

        internal fun prepareIntent(context: Context, form: SignUpForm): Intent {
            return Intent(context, SignUpReviewStepActivity::class.java).apply {
                putExtra(KEY_FORM, form)
            }
        }
    }
}