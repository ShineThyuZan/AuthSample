package com.galaxytechno.chat.presentation.single_activity

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.galaxytechno.chat.R

class LoadingDialog (
    context: Context
) : AlertDialog(context) {

    private val messageTextView: TextView

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_loading,null)
        messageTextView = view.findViewById(R.id.tv_loading)
        setView(view)
    }

    override fun setMessage(message: CharSequence?) {
        this.messageTextView.text = message.toString()
    }


}