package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.ContentFrameLayout

fun Activity.hideKeyboard() {
    val inputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = this.currentFocus ?: View(this)
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

/**
 * Реализация взята из stackoverflow
 * @see https://stackoverflow.com/questions/4745988/how-do-i-detect-if-software-keyboard-is-visible-on-android-device
 * */
fun Activity.isKeyboardOpen(): Boolean {
    var isKeyboardShowing = false
    val window = findViewById<ContentFrameLayout>(android.R.id.content)

    val r = Rect()
    window.getWindowVisibleDisplayFrame(r)

    val screenHeight = window.rootView.height
    val keypadHeight = screenHeight - r.bottom
    if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
        // keyboard is opened
        if (!isKeyboardShowing) {
            isKeyboardShowing = true
        }
    } else {
        // keyboard is closed
        if (isKeyboardShowing) {
            isKeyboardShowing = false
        }
    }
    return isKeyboardShowing
}

fun Activity.isKeyboardClosed(): Boolean {
    return !isKeyboardOpen()
}