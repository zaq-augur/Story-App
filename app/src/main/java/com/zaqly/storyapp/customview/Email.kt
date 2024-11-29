package com.zaqly.storyapp.customview

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Patterns
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText

class Email @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : TextInputEditText(context, attrs, defStyleAttr) {

    init {
        setPadding(25, 26, 17, 26)
        doOnTextChanged { text, _, _, _ ->
            if (!isValidEmail(text.toString())) {
                error = "Format email tidak valid"
            } else {
                error = null
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
