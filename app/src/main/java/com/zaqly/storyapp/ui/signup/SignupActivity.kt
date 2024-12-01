package com.zaqly.storyapp.ui.signup

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.zaqly.storyapp.network.repository.AuthRepository
import com.zaqly.storyapp.databinding.ActivitySignupBinding
import com.zaqly.storyapp.network.api.ApiConfig
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    private val registerViewModel: SignupViewModel by viewModels{
        SignupViewModelFactory(AuthRepository(ApiConfig.getApiService()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignup.setOnClickListener {
            val name = binding.edNama.text.toString().trim()
            val email = binding.edEmail.text.toString().trim()
            val password = binding.edPassword.text.toString().trim()

            val isNameValid = binding.edNama.validateUsername()
            val isEmailValid = binding.edEmail.validateEmail()
            val isPasswordValid = binding.edPassword.validatePassword()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
            } else if (isNameValid && isEmailValid && isPasswordValid) {
                registerViewModel.register(name, email, password)
            } else {
                Toast.makeText(this, "Perbaiki input sebelum melanjutkan", Toast.LENGTH_SHORT).show()
            }
        }

        observeRegisterResult()
        observeLoadingState()
    }

    private fun observeLoadingState() {
        lifecycleScope.launch {
            registerViewModel.isLoading.collect { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
    }

    private fun observeRegisterResult() {
        lifecycleScope.launch {
            registerViewModel.registerState.collect { response ->
                response?.let {
                    if (!it.error!!) {
                        Toast.makeText(this@SignupActivity, "Register berhasil: ${it.message}", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@SignupActivity, "Register gagal: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
