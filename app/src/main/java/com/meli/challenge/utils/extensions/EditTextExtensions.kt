package com.meli.challenge.utils.extensions

import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import com.meli.challenge.utils.TextInput
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart

fun EditText.textEvents(): Flow<TextInput.TextEvent> = combine(
    afterTextChanges(),
    focusChanges().onStart {
        awaitNextLayout()
        emit(isFocusable)
    },
    TextInput::TextEvent,
)

fun EditText.afterTextChanges(startWithDefault: Boolean = false): Flow<String> = callbackFlow {
    val textWatcher = doOnTextChanged { text, _, _, _ ->
        trySend(text?.toString().orEmpty())
    }
    awaitClose { removeTextChangedListener(textWatcher) }
}.onStart {
    if (startWithDefault) emit(text.toString())
}

fun EditText.focusChanges(): Flow<Boolean> = callbackFlow {
    setOnFocusChangeListener { _, hasFocus ->
        trySend(hasFocus)
    }
    awaitClose { onFocusChangeListener = null }
}
