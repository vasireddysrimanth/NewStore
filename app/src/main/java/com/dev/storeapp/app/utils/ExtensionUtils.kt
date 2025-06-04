package com.dev.storeapp.app.utils

import android.view.View
import android.widget.TextView


fun View.visible() = if(visibility != View.VISIBLE) visibility = View.VISIBLE else Unit

fun View.gone() = if(visibility != View.GONE) visibility = View.GONE else Unit

fun View.invisible() = if(visibility != View.INVISIBLE) visibility = View.INVISIBLE else Unit

fun Boolean.toInt() = if (this) 1 else 0

fun Int.toBoolean() = this > 0


fun String.safeInt(fallback: Int = 0): Int {
    return this.toIntOrNull() ?: fallback
}

fun String.safeDouble(fallback: Double): Double {
    return this.toDoubleOrNull() ?: fallback
}

fun Double.isZero(): Boolean {
    return this == 0.0
}

fun Double.isGreaterThanZero(): Boolean {
    return this > 0.0
}

fun Double.isLessThanZeroOrZero(): Boolean {
    return this <= 0.0
}

fun Double.isLessThanOrZero(): Boolean {
    return this <= 0.00
}


fun TextView.setNonEmptyText(s: String?) {
    text = if (!s.isNullOrEmpty()) {
        s
    } else ""
}

fun String?.getNonEmptyText(): String {
    return if (!this.isNullOrEmpty()) {
        this
    } else ""
}

fun Double?.notNullDouble(): Double {
    return this ?: 0.0
}
fun Double?.notNullDoubleDivisor(): Double {
    return this ?: 1.0
}

fun String?.parseDouble(): Double {
    return try {
        this?.toDouble() ?: 0.0
        // Now, doubleValue contains the double value of the string
    } catch (e: Exception) {
        0.0
    }
}

fun Int?.getNonEmptyInt(): Int {
    return this ?: 0
}

fun Int?.getNonEmptyInt(fallback: Int): Int {
    return this ?: fallback
}

fun Int?.getIntByOne(): Int {
    return this ?: 1
}

