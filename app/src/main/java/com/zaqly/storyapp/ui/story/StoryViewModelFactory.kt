package com.zaqly.storyapp.ui.story

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zaqly.storyapp.injection.Injection
import kotlinx.coroutines.runBlocking

class StoryViewModelFactory (private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return runBlocking {
                val repository = Injection.provideRepository(context)
                StoryViewModel(repository)
            } as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
