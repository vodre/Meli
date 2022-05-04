package com.meli.challenge.utils.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber

fun View.getInputMethodManager(): InputMethodManager? = context.getSystemService()

val View.layoutInflater: LayoutInflater get() = LayoutInflater.from(context)

fun View.showSoftKeyboard() {
    this.requestFocus()
    post {
        getInputMethodManager()?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun View.hideSoftKeyboard() {
    post {
        getInputMethodManager()?.hideSoftInputFromWindow(this.windowToken, 0)
        clearFocus()
    }
}

fun View.isKeyboardVisible(): Boolean {
    // This force-unwrap is safe from API 21+
    val rootWindowInsets = ViewCompat.getRootWindowInsets(this)!!
    return rootWindowInsets.isVisible(WindowInsetsCompat.Type.ime())
}

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun View.awaitNextLayout() = suspendCancellableCoroutine<Unit> { cont ->
    // This lambda is invoked immediately, allowing us to create a callback/listener
    val listener = object : View.OnLayoutChangeListener {
        override fun onLayoutChange(
            view: View?,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
            oldLeft: Int,
            oldTop: Int,
            oldRight: Int,
            oldBottom: Int
        ) {
            // The next layout has happened! First remove the listener to not leak the coroutine
            view?.removeOnLayoutChangeListener(this)
            // Finally resume the continuation, and wake the coroutine up
            cont.resume(Unit, Timber::w)
        }
    }

    // If the coroutine is cancelled, remove the listener
    cont.invokeOnCancellation { removeOnLayoutChangeListener(listener) }
    // And finally add the listener to view
    addOnLayoutChangeListener(listener)

    // The coroutine will now be suspended. It will only be resumed
    // when calling cont.resume() in the listener above
}

inline var View.isVisible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.INVISIBLE
    }
