package com.zaqly.storyapp.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.TranslateAnimation
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.zaqly.storyapp.R

class Password @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : TextInputEditText(context, attrs, defStyleAttr) {

    private var isPasswordVisible: Boolean = false
    private var visibilityToggleIcon: Drawable? = null

    init {
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        setPadding(25, 26, 17, 26)

        visibilityToggleIcon = ContextCompat.getDrawable(context, R.drawable.baseline_visibility_off_24)
        setCompoundDrawablesWithIntrinsicBounds(null, null, visibilityToggleIcon, null)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                validatePasswordInstant()
            }
        })
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            if (it.action == MotionEvent.ACTION_UP) {
                val drawableEnd = compoundDrawables[2]
                drawableEnd?.let { drawable ->
                    val drawableWidth = drawable.intrinsicWidth
                    if (it.rawX >= (right - drawableWidth)) {
                        togglePasswordVisibility()
                        return true
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        inputType = if (isPasswordVisible) {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            if (isPasswordVisible) {
                ContextCompat.getDrawable(context, R.drawable.baseline_visibility_24)
            } else {
                ContextCompat.getDrawable(context, R.drawable.baseline_visibility_off_24)
            },
            null
        )
        setSelection(text?.length ?: 0)
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

    private fun validatePasswordInstant() {
        val password = text?.toString()
        if (password.isNullOrEmpty() || password.length < 8) {
            error = "Password harus terdiri dari minimal 8 karakter"
        } else {
            error = null
        }
    }

    fun validatePassword(): Boolean {
        val password = text?.toString()
        return if (password.isNullOrEmpty() || password.length < 8) {
            error = "Password harus terdiri dari minimal 8 karakter"
            shakeAnimation()
            false
        } else {
            error = null
            true
        }
    }
}

