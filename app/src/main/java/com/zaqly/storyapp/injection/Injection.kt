package com.zaqly.storyapp.injection

import android.content.Context
import android.util.Log
import com.zaqly.storyapp.network.api.ApiConfig
import com.zaqly.storyapp.network.repository.StoryRepository
import com.zaqly.storyapp.network.repository.UserPreferences

object Injection {
    suspend fun provideRepository(context: Context): StoryRepository {
        val pref = UserPreferences.getInstance(context)

        val user = pref.getUser()
        Log.d("Injection", "Token ditemukan: ${user.token}")

        val apiService = ApiConfig.getApiService(user.token)
        Log.d("Injection", "ApiService berhasil dibuat dengan token")

        return StoryRepository.getInstance(apiService, pref)

    }
}