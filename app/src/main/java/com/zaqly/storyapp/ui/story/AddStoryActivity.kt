package com.zaqly.storyapp.ui.story

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.zaqly.storyapp.databinding.ActivityAddStoryBinding
import com.zaqly.storyapp.ui.main.MainActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private var currentImageUri: Uri? = null

    private val storyViewModel: StoryViewModel by viewModels {
        StoryViewModelFactory(this)
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        } else {
            currentImageUri = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonGallery.setOnClickListener { startGallery() }
        binding.buttonCamera.setOnClickListener { startCamera() }
        binding.buttonAdd.setOnClickListener { uploadImage() }

        observeViewModel()
    }

    private fun observeViewModel() {
        storyViewModel.uploadResult.observe(this) { result ->
            if (result != null) {
                showToast("Cerita berhasil diunggah!")
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }

        storyViewModel.errorMessage.observe(this) { message ->
            if (message != null) {
                showToast(message)
            }
        }

        storyViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private fun uploadImage() {
        if (currentImageUri == null) {
            showToast("Pilih gambar terlebih dahulu!")
            return
        }

        val description = binding.edAddDescription.text.toString().trim()
        if (description.isEmpty()) {
            showToast("Deskripsi tidak boleh kosong!")
            return
        }
        val compressedUri = compressImage(currentImageUri!!)
        val imageFile = uriToFile(compressedUri, this)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        storyViewModel.uploadStory(multipartBody, requestBody)
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivAddPhotoPreview.setImageURI(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progresbarAdd.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun compressImage(uri: Uri): Uri {
        val originalBitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
        val outputStream = ByteArrayOutputStream()
        originalBitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
        val compressedImage = outputStream.toByteArray()

        val file = File(cacheDir, "compressed_image.jpg")
        val fos = FileOutputStream(file)
        fos.write(compressedImage)
        fos.flush()
        fos.close()
        return Uri.fromFile(file)
    }
}
