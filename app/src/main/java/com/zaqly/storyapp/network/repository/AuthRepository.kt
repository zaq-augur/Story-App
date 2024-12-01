package com.zaqly.storyapp.network.repository

import com.zaqly.storyapp.network.api.ApiService
import com.zaqly.storyapp.network.response.LoginResponse
import com.zaqly.storyapp.network.response.RegisterResponse

class AuthRepository(private val apiService: ApiService) {

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        val response = apiService.register(name, email, password)
        return if (response.error == true) {
            RegisterResponse(error = true, message = response.message)
        } else {
            response
        }
    }

    suspend fun login(email: String, password: String): LoginResponse {
        val response = apiService.login(email, password)
        return if (response.error == true) {
            LoginResponse(error = true, message = response.message, loginResult = response.loginResult)
        } else {
            response
        }
    }

}


