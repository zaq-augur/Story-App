package com.zaqly.storyapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.zaqly.storyapp.network.repository.AuthRepository
import com.zaqly.storyapp.databinding.ActivityLoginBinding
import com.zaqly.storyapp.network.api.ApiConfig
import com.zaqly.storyapp.network.response.LoginResponse
import com.zaqly.storyapp.ui.main.MainActivity
import com.zaqly.storyapp.ui.signup.SignupActivity
import com.zaqly.storyapp.network.repository.UserPreferences
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityLoginBinding
    private lateinit var userPreferences: UserPreferences
    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(AuthRepository(ApiConfig.getApiService()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        userPreferences = UserPreferences.getInstance(applicationContext)

        checkForSavedToken()
        initializeUI()
        observeLoginState()
    }

    private fun checkForSavedToken() {
        lifecycleScope.launch {
            val savedToken = userPreferences.token.firstOrNull()
            Log.d("LoginActivity", "Saved token: $savedToken")
            if (!savedToken.isNullOrEmpty()) {
                moveToMainScreen()
            }
        }
    }

    private fun initializeUI() {
        with(viewBinding) {
            tvSignup.setOnClickListener {
                navigateToSignup()
            }

            btnLogin.setOnClickListener {
                val emailInput = edEmail.text.toString().trim()
                val passwordInput = edPassword.text.toString().trim()
                validateInputsAndLogin(emailInput, passwordInput)
            }
        }
    }

    private fun validateInputsAndLogin(email: String, password: String) {
        when {
            email.isEmpty() || password.isEmpty() -> {
                displayMessage("Email dan Password tidak boleh kosong.")
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                displayMessage("Format email tidak valid.")
            }
            else -> {
                showProgressBar()

                loginViewModel.performLogin(email, password)
            }
        }
    }


    private fun observeLoginState() {
        lifecycleScope.launchWhenStarted {
            loginViewModel.state.collect { loginResponse ->
                Log.d("LoginState", "Received login response: $loginResponse")
                hideProgressBar()
                when {
                    loginResponse == null -> {
                        Log.d("LoginState", "Processing login")
                    }
                    loginResponse.error == false -> {
                        Log.d("LoginState", "Login successful")
                        processLoginResult(loginResponse)
                    }
                    else -> {
                        Log.d("LoginState", "Login failed")
                        displayMessage("Login failed: ${loginResponse.message}")
                    }
                }
            }
        }
    }

    private fun showProgressBar() {
        viewBinding.progressBar.visibility = android.view.View.VISIBLE
    }
    private fun hideProgressBar() {
        viewBinding.progressBar.visibility = android.view.View.GONE
    }


    private fun processLoginResult(response: LoginResponse) {
        if (response.error == false) {
            displayMessage("Selamat datang, ${response.loginResult.name}")
            saveUserCredentials(response.loginResult.token, response.loginResult.name)
            moveToMainScreen()
        } else {
            displayMessage("Login gagal: ${response.message}")
        }
    }

    private fun saveUserCredentials(token: String?, username: String) {
        lifecycleScope.launch {
            if (!token.isNullOrEmpty()) {
                userPreferences.saveToken(token)
                userPreferences.saveUserName(username)
                Log.d("LoginActivity", "Token berhasil disimpan: $token")
            } else {
                Log.e("LoginActivity", "Token tidak valid atau kosong.")
            }
        }
    }

    private fun moveToMainScreen() {
        Intent(this, MainActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }

    private fun navigateToSignup() {
        Intent(this, SignupActivity::class.java).also {
            startActivity(it)
        }
    }

    private fun displayMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
