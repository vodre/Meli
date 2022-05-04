package com.meli.challenge.utils.extensions

import androidx.fragment.app.Fragment

fun Fragment.hideSoftKeyboard() {
    view?.hideSoftKeyboard()
}

fun Fragment.isSoftKeyboardVisible(): Boolean = view?.isKeyboardVisible() ?: false
