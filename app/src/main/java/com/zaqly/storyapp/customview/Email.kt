package com.zaqly.storyapp.customview

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.animation.TranslateAnimation
import com.google.android.material.textfield.TextInputEditText

class Email @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : TextInputEditText(context, attrs, defStyleAttr) {

    private var isEmailValid: Boolean = true

    init {
        setPadding(25, 26, 17, 26)

        // Tambahkan TextWatcher untuk validasi instan
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                validateEmailInstant()
            }
        })
    }

    fun validateEmail(): Boolean {
        val email = text.toString().trim()
        val isValid = isValidEmail(email)

        if (!isValid) {
            if (isEmailValid) error = "Format email tidak valid"
            shakeAnimation()
        } else {
            error = null
        }
        isEmailValid = isValid
        return isValid
    }

    private fun validateEmailInstant() {
        val email = text.toString().trim()
        if (!isValidEmail(email)) {
            error = "Format email tidak valid"
        } else {
            error = null
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun shakeAnimation() {
        val animation = TranslateAnimation(-10f, 10f, 0f, 0f)
        animation.duration = 50
        animation.repeatMode = TranslateAnimation.REVERSE
        animation.repeatCount = 3
        startAnimation(animation)

        Handler(Looper.getMainLooper()).postDelayed({
            requestFocus()
        }, 150)
    }
}

