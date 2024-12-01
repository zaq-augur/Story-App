package com.zaqly.storyapp.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.animation.TranslateAnimation
import com.google.android.material.textfield.TextInputEditText

class Username @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : TextInputEditText(context, attrs, defStyleAttr) {

    init {
        setPadding(25, 26, 17, 26)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                validateUsernameInstant()
            }
        })
    }

    fun validateUsername(): Boolean {
        val username = text.toString().trim()
        return when {
            username.isEmpty() -> {
                error = "Nama pengguna tidak boleh kosong"
                shakeAnimation()
                false
            }
            username.length < 3 -> {
                error = "Nama pengguna minimal 3 karakter"
                shakeAnimation()
                false
            }
            else -> {
                error = null
                true
            }
        }
    }

    private fun validateUsernameInstant() {
        val username = text.toString().trim()
        when {
            username.isEmpty() -> error = "Nama pengguna tidak boleh kosong"
            username.length < 3 -> error = "Nama pengguna minimal 3 karakter"
            else -> error = null
        }
    }

    private fun shakeAnimation() {
        val animation = TranslateAnimation(-10f, 10f, 0f, 0f)
        animation.duration = 50
        animation.repeatMode = TranslateAnimation.REVERSE
        animation.repeatCount = 3
        startAnimation(animation)
    }
}
