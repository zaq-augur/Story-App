package com.zaqly.storyapp.ui.signup
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.zaqly.storyapp.data.remote.repository.AuthRepository
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
            val name = binding.edName.text.toString().trim()
            val email = binding.edEmail.text.toString().trim()
            val password = binding.edPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
            } else {
                registerViewModel.register(name, email, password)
            }
        }

        observeRegisterResult()


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
