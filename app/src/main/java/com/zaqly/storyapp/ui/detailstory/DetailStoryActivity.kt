package com.zaqly.storyapp.ui.detailstory

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.zaqly.storyapp.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setupSharedElementTransition()
        setupUI()
    }

    private fun setupBinding() {
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupSharedElementTransition() {
        val transition = TransitionInflater.from(this)
            .inflateTransition(android.R.transition.move)
        window.sharedElementEnterTransition = transition
        window.sharedElementReturnTransition = transition

        window.enterTransition = TransitionInflater.from(this)
            .inflateTransition(android.R.transition.fade)
    }

    private fun setupUI() {
        val name = intent.getStringExtra("STORY_NAME")
        val description = intent.getStringExtra("STORY_DESCRIPTION")
        val photoUrl = intent.getStringExtra("STORY_PHOTO")

        // Menampilkan data ke elemen UI
        binding.tvDetailName.text = name ?: "Nama tidak tersedia"
        binding.tvDescription.text = description ?: "Deskripsi tidak tersedia"
        loadImage(photoUrl)
    }
    private fun loadImage(photoUrl: String?) {
        Glide.with(this)
            .load(photoUrl)
            .into(binding.ivDetailPhoto)
    }

    override fun onEnterAnimationComplete() {
        super.onEnterAnimationComplete()
        binding.ivDetailPhoto.apply {
            alpha = 0f
            translationY = 50f
            animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(700)
                .setStartDelay(500)
                .start()
        }
        binding.tvDetailName.apply {
            alpha = 0f
            translationY = 50f
            animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(700)
                .setStartDelay(500)
                .start()
        }
        binding.tvDescription.apply {
            alpha = 0f
            translationY = 50f
            animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(700)
                .setStartDelay(500)
                .start()
        }
    }
}
