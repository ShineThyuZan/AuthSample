package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.tabs.all

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.galaxytechno.chat.R
import com.galaxytechno.chat.model.dto.Country
import com.galaxytechno.chat.model.dto.UserVos
import com.galaxytechno.chat.model.vos.FriListVos
import com.galaxytechno.chat.presentation.base.BaseViewHolder

class ChatRecentMsgViewHolder(
    itemView: View,
    private val delegate: ChatRecentMsgDelegate
) :
    BaseViewHolder<UserVos>(itemView) {
    var chatRecentMsg: TextView? = null
    var imageViewRemove: ImageButton? = null

    override fun setData(data: UserVos) {
        mData = data
        chatRecentMsg = itemView.findViewById<View>(R.id.tvChatRecentMsg) as TextView
        chatRecentMsg!!.text = mData.name
       // imageViewRemove = itemView.findViewById<View>(R.id.ivRemove) as ImageButton


    }

    override fun onClick(v: View?) {
        delegate.onClickedRecentMsg(mData)
    }
}