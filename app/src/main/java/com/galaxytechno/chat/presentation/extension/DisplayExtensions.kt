package com.galaxytechno.chat.presentation.extension

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun AppCompatActivity.displaySnack(
    msg: String = "Just an Activity Snack",
    duration: Int = Snackbar.LENGTH_SHORT
) = Snackbar.make(findViewById(android.R.id.content)!!, msg, duration).show()

fun AppCompatActivity.displaySnackTop(
    msg: String = "Just an Activity Snack",
    duration: Int = Snackbar.LENGTH_SHORT
): Snackbar {

    val snack = Snackbar.make(findViewById(android.R.id.content)!!, msg, duration)
    snack.setBackgroundTint(
        ContextCompat.getColor(this, android.R.color.transparent)
    )
    val view: View = snack.view
    val params = view.layoutParams as FrameLayout.LayoutParams
    params.gravity = Gravity.TOP
    view.layoutParams = params
    snack.show()
    return snack
}

fun View.displaySnack(
    msg: String = "Just a View Snack",
    duration: Int = Snackbar.LENGTH_SHORT
) = Snackbar.make(this, msg, duration).show()


fun View.displaySnackAction(
    msg: String = "Just a View Snack",
    actionText: String = "Done",
    onActionClick: (View) -> Unit = {},
    duration: Int = Snackbar.LENGTH_SHORT
) = Snackbar.make(this, msg, duration).setAction(
    actionText,
    onActionClick
).show()


fun View.displaySnackLongTime(
    msg: String = "Just a View Snack",
    duration: Int = Snackbar.LENGTH_LONG
) = Snackbar.make(this, msg, duration).show()

fun View.displaySnackTop(
    msg: String = "Just an Activity Snack",
    duration: Int = Snackbar.LENGTH_LONG
): Snackbar {

    val snack = Snackbar.make(this, msg, duration)
    /*snack.setBackgroundTint(
        ContextCompat.getColor(this.context, android.R.color.transparent)
    )*/
    val view: View = snack.view
    val params = view.layoutParams as FrameLayout.LayoutParams
    params.gravity = Gravity.TOP
//  params.topMargin = android.R.attr.actionBarSize
    view.layoutParams = params
    snack.show()
    return snack
}

fun Context.displayToast(
    message: String,
    duration: Int = Toast.LENGTH_SHORT
) = Toast.makeText(this, message, duration).show()

fun Fragment.displayToast(
    message: String,
    duration: Int = Toast.LENGTH_SHORT
) = Toast.makeText(requireActivity(), message, duration).show()