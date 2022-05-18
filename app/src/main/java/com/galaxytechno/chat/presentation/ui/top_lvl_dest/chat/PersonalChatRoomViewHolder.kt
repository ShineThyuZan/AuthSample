package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat

import android.view.View
import android.widget.TextView
import com.galaxytechno.chat.R
import com.galaxytechno.chat.model.dto.MessageObj
import com.galaxytechno.chat.presentation.base.BaseViewHolder

class PersonalChatRoomViewHolder(
    itemView: View,
    private val delegate: MessageSelectedDelegate
) : BaseViewHolder<MessageObj>(itemView) {

    val text = itemView.findViewById<TextView>(R.id.text)


    override fun setData(data: MessageObj) {


    }

    override fun onClick(v: View?) {
        delegate.onClickSelectedMessage(mData)
    }
}