package com.galaxytechno.chat.presentation.extension

import android.text.Editable
import android.widget.Button
import android.widget.EditText
import com.galaxytechno.chat.R
import com.galaxytechno.chat.common.Constant
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

fun Button.setActive(isActive: Boolean) {
    this.isEnabled = isActive
    this.isClickable = isActive
    this.isFocusable = isActive
}

fun TextInputLayout.setCloseable(text: Editable?) {

    text?.let {
        this.apply {
            setEndIconDrawable(R.drawable.ic_close)
            endIconMode = TextInputLayout.END_ICON_CUSTOM
            setEndIconOnClickListener {
                text.clear()
                endIconMode = TextInputLayout.END_ICON_NONE
            }
        }
    } ?: run {
        this.endIconMode = TextInputLayout.END_ICON_NONE
    }
}

fun EditText.setActive(isActive: Boolean) {
    this.isEnabled = isActive
    this.isClickable = isActive
}

fun EditText.isVerifiedName(): Boolean {
    return !this.text.isNullOrEmpty()
}

fun EditText.isVerifiedEmail(): Boolean {
    if (this.text.isNullOrEmpty()) return false
    val text = "${this.text}@gmail.com"
    return android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()
}

fun EditText.isVerifiedEmailAddress1(): Boolean {

    if (this.text.isNullOrEmpty()) return false

    val input = "${this.text}@gmail.com"

    return Pattern.compile(Constant.EMAIL_REGEX_ADDRESS).matcher(input).matches()

}

fun EditText.isVerifiedEmailAddress2(): Boolean {

    if (this.text.isNullOrEmpty()) return false

    val input = "${this.text}@gmail.com"

    return Constant.EMAIL_REGEX.toRegex().matches(input)

}

fun EditText.isVerifiedPwd(): Boolean {
    if (this.text.isNullOrEmpty()) return false
    return this.text.trim().length >= 8
}

fun EditText.isIdentifiedPwd(pwd: String?): Boolean {
    return this.text.trim().toString() == pwd
}
fun EditText.isVerifiedAnswer() : Boolean{
    return this.text.isNotEmpty()
}

fun EditText.isVerifiedPhone(): Boolean {

    val input = this.text.trim()

    when (input.length) {
        9 -> {
            val twoPrefix = input.substring(0, 2)
            if (prefixLength9.contains(twoPrefix)) return true
        }
        8 -> {
            val twoPrefix = input.substring(0, 2)
            if (prefixLength8.contains(twoPrefix)) return true
        }
        7 -> {
            val twoPrefix = input.substring(0, 2)
            if (prefixLength7.contains(twoPrefix)) return true
        }
        6 -> {
            val twoPrefix = input.substring(0, 2)
            if (prefixLength6.contains(twoPrefix)) return true
        }
        else -> {
            return false
        }
    }
    return false
}

fun Editable.isVerifiedPhone(): Boolean {
    when (this.length) {
        9 -> {
            val twoPrefix = this.substring(0, 2)
            if (prefixLength9.contains(twoPrefix)) return true
        }
        8 -> {
            val twoPrefix = this.substring(0, 2)
            if (prefixLength8.contains(twoPrefix)) return true
        }
        7 -> {
            val twoPrefix = this.substring(0, 2)
            if (prefixLength7.contains(twoPrefix)) return true
        }
        6 -> {
            val twoPrefix = this.substring(0, 2)
            if (prefixLength6.contains(twoPrefix)) return true
        }
        else -> {
            return false
        }
    }
    return false
}


val prefixLength9 = listOf(
    "25",
    "26",
    "34",
    "40",
    "42",
    "44",
    "45",
    "46",
    "48",
    "65",
    "66",
    "67",
    "68",
    "69",
    "75",
    "76",
    "77",
    "78",
    "79",
    "88",
    "89",
    "94",
    "95",
    "96",
    "97",
    "98",
    "99"
)
val prefixLength8 = listOf(
    "22",
    "30",
    "31",
    "33",
    "34",
    "35",
    "36",
    "37",
    "38",
    "39",
    "41",
    "43",
    "47",
    "49",
    "73",
    "91"
)
val prefixLength7 = listOf(
    "20",
    "21",
    "22",
    "23",
    "24",
    "50",
    "51",
    "52",
    "53",
    "54",
    "55",
    "56",
    "81",
    "83",
    "84",
    "85",
    "86",
    "87"
)
val prefixLength6 = listOf(
    "70",
    "71"
)
