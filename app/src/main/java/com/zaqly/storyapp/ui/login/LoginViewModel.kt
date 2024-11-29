package com.zaqly.storyapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaqly.storyapp.data.remote.repository.AuthRepository
import com.zaqly.storyapp.network.response.LoginResponse
import com.zaqly.storyapp.network.response.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _state = MutableStateFlow<LoginResponse?>(null)
    val state: StateFlow<LoginResponse?> get() = _state

    fun performLogin(email: String, password: String) {
        viewModelScope.launch {
            _state.emit(null)
            val response = try {
                repository.login(email, password)
            } catch (exception: Exception) {
                LoginResponse(
                    error = true,
                    message = exception.localizedMessage ?: "Kesalahan tidak diketahui",
                    loginResult = User("", "", "")
                )
            }
            _state.emit(response)
        }
    }
}

