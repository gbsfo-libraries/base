package com.core.base.util

import android.content.res.Resources
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewTreeObserver
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.*
import android.widget.EditText
import androidx.core.util.PatternsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController

fun EditText.validatePhoneNumber(): Boolean {
    val phoneNumber = this.text.toString().replaceSpaces()

    return if (TextUtils.isEmpty(phoneNumber) || !phoneNumber.startsWith("+380") || phoneNumber.length < 13) {
        false
    } else {
        android.util.Patterns.PHONE.matcher(phoneNumber).matches()
    }
}

fun String.tokenPattern() = "Bearer $this"

fun View.OnKeyListener.validateCodeInput(keyInput: Int): Boolean {

    val validInput = arrayOf(
        KeyEvent.KEYCODE_0,
        KeyEvent.KEYCODE_1,
        KeyEvent.KEYCODE_2,
        KeyEvent.KEYCODE_3,
        KeyEvent.KEYCODE_4,
        KeyEvent.KEYCODE_6,
        KeyEvent.KEYCODE_7,
        KeyEvent.KEYCODE_8,
        KeyEvent.KEYCODE_9
    )
    return validInput.contains(keyInput)
}

fun Float.dpToPx(displatMetrics: DisplayMetrics): Float =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, displatMetrics)

fun Char.isSpace(): Boolean = this.toString() == " "

fun String.replaceSpaces() = this.replace(" ", "")

fun String.validateName(): Boolean = this.length > 2

fun String.validateSurName(): Boolean = this.length > 2

fun String.validateEmail(): Boolean =
    this.isNotEmpty() && PatternsCompat.EMAIL_ADDRESS.matcher(this).matches()

fun String.convertToPhoneNumberWithReplace(): String {
    val mask = "XXXX XX XXX XX XX"
    val text = this.replaceSpaces()

    val resultText = StringBuilder()
    resultText.append(mask)
    var xIndex: Int
    for (digit in text.toCharArray()) {
        xIndex = resultText.indexOf('X')
        resultText.setCharAt(xIndex, digit)
    }

    var end = resultText.indexOf('X')

    if (end == -1) {
        end = mask.length
    }

    return resultText.toString().substring(0, end)
}

fun FragmentManager.getVisibleFragment(): Fragment? {
    val fragments = this.fragments
    for (fragment in fragments) {
        if (fragment != null && fragment.isVisible)
            return fragment
    }
    return null
}

val Int.savingsProgress: Int
    get() = (this * 2000)

fun NavController.navigateClearStack(rootFragmentId: Int, destinationId: Int) {
    this.popBackStack(rootFragmentId, false)
    this.navigate(destinationId)
}


fun Boolean.toInt() = if (this) 1 else 0

val Int.toPx: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.toDp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Float.toPx: Float
    get() = (this / Resources.getSystem().displayMetrics.density)

val Float.toDp: Float
    get() = (this * Resources.getSystem().displayMetrics.density)

val Long.secToMs: Long
    get() = (this * 1000)

val Float.secToMs: Long
    get() = (this * 1000).toLong()

val String.packageName: String
    get() = (this.substring(0, this.lastIndexOf(".")))

fun Drawable.maskColor(color: Int) {
    this.mutate()
    this.clearColorFilter()
    this.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
}

fun String.formatAsUrl() =
    if (!this.startsWith("http://") && !this.startsWith("https://")) "https://$this" else this

fun View?.applyGlobalLayoutListener(attachedExpr: (it: View?) -> Unit) {
    this?.viewTreeObserver?.addOnGlobalLayoutListener(object :
        ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            this@applyGlobalLayoutListener.viewTreeObserver.removeOnGlobalLayoutListener(this)
            attachedExpr(this@applyGlobalLayoutListener)
        }
    }) ?: attachedExpr(null)
}
