package com.zaqly.storyapp.customview

import android.content.Context
import android.util.AttributeSet
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText

class Password @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : TextInputEditText(context, attrs, defStyleAttr) {

    init {
        setPadding(25, 26, 17, 26)
        doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty() || text.length < 8) {
                error = "Password harus terdiri dari minimal 8 karakter"
            } else {
                error = null
            }
        }
    }
}
