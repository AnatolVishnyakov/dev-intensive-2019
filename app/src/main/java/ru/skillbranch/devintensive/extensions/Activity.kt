package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout

var isKeyboardShowing = false

fun Activity.hideKeyboard() {
    val inputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = this.currentFocus ?: View(this)
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.isKeyboardOpen(): Boolean {
    val rootView = findViewById<LinearLayout>(android.R.id.content)

    val r = Rect()
    rootView.getWindowVisibleDisplayFrame(r)

    val screenHeight = rootView.rootView.height
    val keypadHeight = screenHeight - r.bottom
    if (keypadHeight > screenHeight * 0.15) {
        if (!isKeyboardShowing) {
            isKeyboardShowing = true
            return isKeyboardShowing
        }
    }
    return false
}

fun Activity.isKeyboardClosed(): Boolean {
    val rootView = findViewById<LinearLayout>(android.R.id.content)

    val r = Rect()
    rootView.getWindowVisibleDisplayFrame(r)

    val screenHeight = rootView.rootView.height
    val keypadHeight = screenHeight - r.bottom
    if (keypadHeight < screenHeight * 0.15) {
        if (isKeyboardShowing) {
            isKeyboardShowing = false
            return isKeyboardShowing
        }
    }
    return false

}