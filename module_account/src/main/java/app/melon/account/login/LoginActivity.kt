package app.melon.account.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import app.melon.account.R
import app.melon.account.databinding.ActivityLoginBinding
import app.melon.account.signup.SignUpStepFormActivity
import app.melon.util.delegates.viewBinding
import app.melon.util.extensions.afterTextChanged
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class LoginActivity : DaggerAppCompatActivity() {

    private val presetUsername get() = intent.getStringExtra(KEY_USERNAME)
    private val presetPassword get() = intent.getStringExtra(KEY_PASSWORD)

    private val binding: ActivityLoginBinding by viewBinding()

    companion object {
        private const val KEY_USERNAME = "KEY_USERNAME"
        private const val KEY_PASSWORD = "KEY_PASSWORD"

        internal fun prepareIntent(context: Context, username: String, password: String): Intent {
            return Intent(context, LoginActivity::class.java).apply {
                putExtra(KEY_USERNAME, username)
                putExtra(KEY_PASSWORD, password)
            }
        }
    }

    @Inject internal lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            // disable login button unless both username / password is valid
            binding.login.isEnabled = it?.isDataValid == true
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer { result ->
            result ?: return@Observer
            binding.loginLoading.isVisible = false
            result.error?.let { showLoginFailed(it) }
            if (result.success) {
                onLoginSuccess()
            }
        })

        binding.loginCredential.afterTextChanged {
            loginViewModel.loginDataChanged(
                binding.loginCredential.text.toString(),
                binding.loginPassword.text.toString()
            )
        }

        binding.loginPassword.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    binding.loginCredential.text.toString(),
                    binding.loginPassword.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            binding.loginCredential.text.toString(),
                            binding.loginPassword.text.toString()
                        )
                }
                false
            }

            binding.login.setOnClickListener {
                binding.loginLoading.visibility = View.VISIBLE
                loginViewModel.login(binding.loginCredential.text.toString(), binding.loginPassword.text.toString())
            }
        }

        binding.loginCredential.setText(presetUsername.orEmpty())
        binding.loginPassword.setText(presetPassword.orEmpty())
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    private fun onLoginSuccess() {
        setResult(Activity.RESULT_OK)

        //Complete and destroy login activity once successful
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_sign_up -> {
                startActivity(Intent(this, SignUpStepFormActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_login, menu)
        return true
    }
}