package com.zaqly.storyapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.zaqly.storyapp.R
import com.zaqly.storyapp.databinding.ActivityMainBinding
import com.zaqly.storyapp.network.repository.UserPreferences
import com.zaqly.storyapp.ui.adapter.AdapterActivity
import com.zaqly.storyapp.ui.detailstory.DetailStoryActivity
import com.zaqly.storyapp.ui.login.LoginActivity
import com.zaqly.storyapp.ui.story.AddStoryActivity
import com.zaqly.storyapp.ui.story.StoryViewModel
import com.zaqly.storyapp.ui.story.StoryViewModelFactory
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    private val storyViewModel: StoryViewModel by viewModels {
        StoryViewModelFactory(this)
    }

    private val storyAdapter by lazy {
        AdapterActivity { storyItem, imageView, textView ->
            val intent = Intent(this, DetailStoryActivity::class.java).apply {
                putExtra("STORY_NAME", storyItem.name)
                putExtra("STORY_DESCRIPTION", storyItem.description)
                putExtra("STORY_PHOTO", storyItem.photoUrl)
            }

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                Pair(imageView, "shared_image"),
                Pair(textView, "shared_name")
            )

            startActivity(intent, options.toBundle())
        }
    }


    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            val userPreferences = UserPreferences.getInstance(this@MainActivity)
            val token = userPreferences.token.firstOrNull()
            if (token.isNullOrEmpty()) {
                Log.d("MainActivity", "Token tidak ditemukan. Arahkan ke Login.")
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }


        setupRecyclerView()
        observeViewModel()
        storyViewModel.fetchStories()

        binding.btnLogout.setOnClickListener {
            logout()
        }

        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }

    }
    private fun setupRecyclerView() {
        binding.rvListStory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = storyAdapter
        }
    }

    private fun observeViewModel() {

        storyViewModel.stories.observe(this) { stories ->
            storyAdapter.setStories(stories) //
        }

        storyViewModel.isLoading.observe(this) { isLoading ->
            binding.progresbar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        storyViewModel.errorMessage.observe(this) { errorMessage ->
            errorMessage?.let {
                showToast(it) // Tampilkan pesan error jika ada
            }
        }
    }


    private fun logout() {
        lifecycleScope.launch {
            val userPreferences = UserPreferences.getInstance(this@MainActivity)
            userPreferences.clearToken()
            userPreferences.clearUserName()
            Log.d("MainActivity", "Token dan nama pengguna berhasil dihapus")

            Toast.makeText(this@MainActivity, "Logout berhasil", Toast.LENGTH_SHORT).show()

            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}