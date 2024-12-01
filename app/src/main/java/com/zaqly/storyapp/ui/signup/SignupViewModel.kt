package com.zaqly.storyapp.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaqly.storyapp.network.repository.AuthRepository
import com.zaqly.storyapp.network.response.RegisterResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignupViewModel(private val authRepository: AuthRepository): ViewModel() {

    private val _registerState = MutableStateFlow<RegisterResponse?>(null)
    val registerState: StateFlow<RegisterResponse?> = _registerState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = authRepository.register(name, email, password)
                _registerState.value = response
            } catch (e: Exception) {
                _registerState.value = RegisterResponse(error = true, message = e.message)
            } finally {
                _isLoading.value = false
            }
        }
    }
}