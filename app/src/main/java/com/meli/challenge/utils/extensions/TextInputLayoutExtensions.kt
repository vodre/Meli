@file:Suppress("TooManyFunctions")

package com.meli.challenge.utils.extensions

import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Filterable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import com.meli.challenge.R
import com.meli.challenge.utils.TextInput
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.onStart

var TextInputLayout.text: String
    get() = editText?.text.toString()
    set(value) {
        when (editText) {
            is AutoCompleteTextView -> autoCompleteTextView {
                setText(value, false)
            }
            else -> editText?.setText(value)
        }
    }

fun TextInputLayout.showError(@StringRes idRes: Int) = showError(context.getString(idRes))

fun TextInputLayout.showError(message: String) {
    error = message
}

fun TextInputLayout.clearError() {
    error = null
}

fun TextInputLayout.showValidationIcon(isValid: Boolean) {
    val drawable = if (isValid) R.drawable.ic_validation_check else R.drawable.ic_validation_error
    val tint = if (isValid) R.color.green_700 else R.color.red_700

    if (editText?.isFocused == true) {
        showStartIcon(drawable, tint)
    }

    if (isValid) clearError()

    editText?.setOnFocusChangeListener { _, hasFocus ->
        if (hasFocus) {
            showStartIcon(drawable, tint)
        } else {
            hideStartIcon()
        }
    }
}

fun TextInputLayout.showStartIcon(
    @DrawableRes drawableRes: Int,
    @ColorRes colorRes: Int = 0

) {
    setStartIconTintList(ContextCompat.getColorStateList(context, colorRes))
    startIconDrawable = ContextCompat.getDrawable(context, drawableRes)
}

fun TextInputLayout.hideStartIcon() {
    setStartIconTintMode(null)
    startIconDrawable = null
}

fun TextInputLayout.textEvents(): Flow<TextInput.TextEvent> = editText?.textEvents() ?: emptyFlow()

fun TextInputLayout.afterTextChanges(startWithDefault: Boolean = false): Flow<String> =
    editText?.afterTextChanges(startWithDefault) ?: emptyFlow()

fun TextInputLayout.focusChanges(): Flow<Boolean> = editText?.focusChanges() ?: emptyFlow()

fun TextInputLayout.autoCompleteTextView(configure: AutoCompleteTextView.() -> Unit) {
    (editText as? AutoCompleteTextView)?.run(configure)
}

fun <T> TextInputLayout.setAutocompleteArrayAdapter(items: List<T>) {
    autoCompleteTextView {
        val adapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, items)
        setAdapter(adapter)
    }
}

inline fun <reified T> TextInputLayout.dropdownSelections(
    startWithDefault: Boolean = true,
): Flow<T> = callbackFlow {
    if (editText !is AutoCompleteTextView) {
        cancel("EditText must be an AutoCompleteTextView")
        return@callbackFlow
    }

    val autoCompleteTextView = editText as AutoCompleteTextView
    autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
        when (val item = autoCompleteTextView.adapter.getItem(position)) {
            !is T -> cancel("Expected ${T::class.java.name} but got ${item.javaClass.name}")
            else -> trySend(item as T)
        }
    }
    awaitClose { autoCompleteTextView.onItemClickListener = null }
}.onStart {
    if (startWithDefault) {
        (editText as? AutoCompleteTextView)?.getItemFromText()?.let { item ->
            if (item is T) {
                emit(item as T)
            }
        }
    }
}

fun AutoCompleteTextView.getItemFromText(): Any? {
    val string = text.toString()
    val filter = (adapter as Filterable).filter
    for (i in 0 until adapter.count) {
        val item = adapter.getItem(i)
        if (string == filter.convertResultToString(item)) {
            return item
        }
    }
    return null
}
