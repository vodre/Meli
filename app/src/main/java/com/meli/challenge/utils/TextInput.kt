package com.meli.challenge.utils

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.doOnLayout
import com.google.android.material.textfield.TextInputLayout
import com.meli.challenge.R
import com.meli.challenge.utils.extensions.ValidationState
import com.meli.challenge.utils.extensions.hideSoftKeyboard
import com.meli.challenge.utils.extensions.showSoftKeyboard
import timber.log.Timber

class TextInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : TextInputLayout(context, attrs) {
    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TextInput,
            0,
            0
        ).apply {
            try {
                if (getBoolean(R.styleable.TextInput_focusedByDefault, false)) {
                    // Must be done after layout because the EditText cannot be focused immediately
                    doOnLayout {
                        editText?.requestFocus()
                        editText?.showSoftKeyboard()
                    }
                }
            } finally {
                recycle()
            }
        }
    }

    override fun setErrorEnabled(enabled: Boolean) {
        super.setErrorEnabled(enabled)
        if (!enabled) return
        try {
            val errorView = findViewById<TextView>(com.google.android.material.R.id.textinput_error)
            val errorViewParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            val top = resources.getDimension(R.dimen.textInput_caption_marginTop).toInt()
            errorViewParams.setMargins(0, top, 0, 0)
            errorView.layoutParams = errorViewParams
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override fun onViewRemoved(view: View?) {
        // Note: This could be an issue if we are navigating to another screen that is requesting
        // keyboard focus
        hideSoftKeyboard()
        super.onViewRemoved(view)
    }

    fun render(state: ValidationState?) {
        when (state) {
            is ValidationState.Success -> {
                error = null
                isErrorEnabled = false
                helperText = state.message
            }
            is ValidationState.Error -> {
                isErrorEnabled = true
                error = state.message
                helperText = null
            }
            is ValidationState.Changed -> {
                error = null
                isErrorEnabled = false
                helperText = state.message
            }
        }
    }

    data class TextEvent(
        val text: String,
        val isFocused: Boolean
    )
}
